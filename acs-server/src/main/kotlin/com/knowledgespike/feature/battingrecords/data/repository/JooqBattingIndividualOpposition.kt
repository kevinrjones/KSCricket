package com.knowledgespike.feature.battingrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.Players.Companion.PLAYERS
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.references.PLAYERSDATES
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record21
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*
import java.math.BigDecimal

/**
 * JooqBattingIndividualSeries provides utility functions aimed at creating complex
 * database queries for retrieving and processing cricket batting data using JOOQ.
 */
object JooqBattingIndividualOpposition {

    /**
     * Creates a Common Table Expression (CTE) that generates a detailed structured query
     * for cricket batting statistics based on the given search parameters.
     *
     * The method builds a query for selecting batting statistics with various filtering
     * and computed fields such as matches, innings, runs, high scores, strike rate, and more.
     * This aggregated data is grouped and filtered by player, match type, opponents, and other
     * provided criteria.
     *
     * @param searchParameters The validated search parameters for batting statistics,
     * including filters for match type, venue, result, and other criteria.
     * @param tmpBattingDetailsName The temporary table name containing batting details used
     * for computation and query.
     * @param tempTeamsName The temporary table name containing team information that is
     * joined and utilized in the query.
     * @return A `SelectJoinStep` query object that represents the structured SQL query
     * for fetching batting statistics with detailed fields and computations.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpBattingDetailsName: String,
        tempTeamsName: String,
        tempMatchCountsName: String,
    ): SelectJoinStep<Record21<Int?, String?, String?, Int, Int, Int, Int, Int, Int, Double, Int, Int, Int, Int, Int, Int, String, String, Double, Double, BigDecimal>> {


        val completedInnings =
            field(
                "innings.innings",
                Int::class.java
            ).minus(field("innings.notouts", Int::class.java))
        val strikeRate = iif(
            field("innings.balls", Int::class.java).eq(0),
            inline(0.0),
            field("innings.runs", Int::class.java).cast(Double::class.java)
                .div(field("innings.balls", Int::class.java)).mul(100)
        )

        val average = iif(
            completedInnings.eq(0), inline(0.0), trunc(
                field("innings.runs", Int::class.java).cast(Double::class.java).div(
                    completedInnings
                ), 2
            )
        )

        val searchCondition = buildSearchConditions(searchParameters)
        val limitClause = buildLimitClause(field("innings.runs"), searchParameters.pagingParameters.limit)

        val bi = sqrt(average.mul(strikeRate))
        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                year(PLAYERSDATES.DEBUT).`as`("debut"),
                year(PLAYERSDATES.ACTIVEUNTIL).`as`("end"),
                field("matches", Int::class.java),
                field("innings", Int::class.java),
                field("notouts", Int::class.java),
                field("runs", Int::class.java),
                field("highestscore", Double::class.java),
                field("hundreds", Int::class.java),
                field("fifties", Int::class.java),
                field("ducks", Int::class.java),
                field("fours", Int::class.java),
                field("sixes", Int::class.java),
                field("balls", Int::class.java),
                coalesce(field("${tempTeamsName}.teams", String::class.java), inline("Unknown")).`as`("teams"),
                coalesce(field("O.name", String::class.java), inline("Unknown")).`as`("opponents"),
                average.`as`("avg"),
                strikeRate.`as`("sr"),
                bi.`as`("bi")
            ).from(
                select(
                    field("${tempMatchCountsName}.count").`as`("matches"),
                    count(
                        `when`(
                            field("dismissaltype").ne(11)
                                .and(field("dismissaltype").ne(14))
                                .and(field("dismissaltype").ne(15)),
                            1
                        )
                    ).`as`("innings"),
                    field("${tmpBattingDetailsName}.playerId"),
                    field("${tmpBattingDetailsName}.matchType"),
                    field("${tmpBattingDetailsName}.opponentsId"),
                    sum(field("score", Int::class.java)).`as`("runs"),
                    sum(field("notout", Int::class.java)).`as`("notouts"),
                    sum(field("hundred", Int::class.java)).`as`("hundreds"),
                    sum(field("fifty", Int::class.java)).`as`("fifties"),
                    max(field("NotOutAdjustedScore", Int::class.java)).`as`("HighestScore"),
                    sum(field("duck", Int::class.java)).`as`("ducks"),
                    sum(field("fours", Int::class.java)).`as`("fours"),
                    sum(field("sixes", Int::class.java)).`as`("sixes"),
                    computedBalls(),
                ).from(tmpBattingDetailsName)
                    .join(tempMatchCountsName)
                    .on(field("${tempMatchCountsName}.playerid", Int::class.java).eq(field("${tmpBattingDetailsName}.playerid", Int::class.java)))
                    .and(field("${tempMatchCountsName}.OpponentsId", Int::class.java).eq(field("${tmpBattingDetailsName}.OpponentsId", Int::class.java)))
                    .join(MATCHES).on(field("${tmpBattingDetailsName}.matchid", Int::class.java).eq(MATCHES.ID))
                    .join(EXTRAMATCHDETAILS)
                    .on(
                        EXTRAMATCHDETAILS.MATCHID.eq(
                            field(
                                "${tmpBattingDetailsName}.matchid",
                                Int::class.java
                            )
                        )
                    )
                    .and(
                        EXTRAMATCHDETAILS.TEAMID.eq(
                            field(
                                "${tmpBattingDetailsName}.teamid",
                                Int::class.java
                            )
                        )
                    )
                    .and(searchCondition)
                    .groupBy(field("matchtype"), field("playerid"), field("${tmpBattingDetailsName}.opponentsId"))
                    .asTable("innings")
                    .join(PLAYERS).on(PLAYERS.ID.eq(field("innings.playerid", Int::class.java)))
                    .join(PLAYERSDATES).on(PLAYERSDATES.PLAYERID.eq(field("innings.playerid", Int::class.java)))
                    .and(PLAYERSDATES.MATCHTYPE.eq(searchParameters.matchType.value))
                    .leftJoin(tempTeamsName)
                    .on(
                        field("${tempTeamsName}.playerId", Int::class.java)
                            .eq(
                                field("innings.playerid", Int::class.java)
                            )
                    )
                    .and(field("${tempTeamsName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                    .and(field("${tempTeamsName}.opponentsId", Int::class.java).eq(field("innings.OpponentsId", Int::class.java)))
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(field("innings.OpponentsId")))
                    .where(limitClause)
            )

        return cte
    }

}