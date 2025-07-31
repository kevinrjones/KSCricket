package com.knowledgespike.feature.bowlingrecords.data.repository

import com.knowledgespike.db.tables.Players.Companion.PLAYERS
import com.knowledgespike.db.tables.Teams.Companion.TEAMS
import com.knowledgespike.db.tables.references.PLAYERSDATES
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import org.jooq.Record
import org.jooq.SelectJoinStep
import org.jooq.impl.DSL.*

/**
 * An object containing methods to compute and manage cricket bowling career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into bowling performances.
 */
object JooqBowlingIndividualSeason {

    /**
     * Constructs a common table expression (CTE) that aggregates and computes statistical data
     * for bowlers based on the provided search parameters and temporary table names.
     *
     * @param searchParameters The validated search parameters detailing filters such as match types,
     *        opponent information, limits, and other query constraints.
     * @param bowlTempName The name of the temporary table containing bowling statistics.
     * @param teamsTempName The name of the temporary table containing team information.
     * @param tempMatchCountsName The name of the temporary table containing match counts.
     * @return The constructed CTE as a `SelectJoinStep<Record>` object, representing the statistical
     *         results including metrics like average, economic rate, strike rate, bowling index,
     *         and others for the queried players.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        bowlTempName: String,
        teamsTempName: String,
        tempMatchCountsName: String,
    ): SelectJoinStep<Record> {
        val strikeRate = field("innings.balls", Int::class.java).cast(Double::class.java)
            .div(field("innings.wickets", Int::class.java))
        val economy = trunc(
            field("innings.runs", Int::class.java).cast(Double::class.java)
                .div(field("innings.balls", Int::class.java)).mul(6), 2
        )
        val average = trunc(
            field("innings.runs", Int::class.java).cast(Double::class.java)
                .div(field("innings.wickets", Int::class.java)), 2
        )
        val runsPerBall = field("innings.runs", Int::class.java).cast(Double::class.java).mul(100).div(
            field("innings.balls", Int::class.java)
        )
        val bi = sqrt(average.mul(runsPerBall))

        val cte =
            select(
                PLAYERS.ID.`as`("playerid"),
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                year(PLAYERSDATES.DEBUT).`as`("debut"),
                year(PLAYERSDATES.ACTIVEUNTIL).`as`("end"),
                field("innings.matches", Int::class.java).`as`("matches"),
                field("innings.innings", Int::class.java).`as`("innings"),
                field("innings.balls", Int::class.java).`as`("balls"),
                field("innings.maidens", Int::class.java).`as`("maidens"),
                field("innings.runs", Int::class.java).`as`("runs"),
                field("innings.wickets", Int::class.java).`as`("wickets"),
                field("innings.noballs", Int::class.java).`as`("noballs"),
                field("innings.wides", Int::class.java).`as`("wides"),
                field("innings.dots", Int::class.java).`as`("dots"),
                field("innings.fours", Int::class.java).`as`("fours"),
                field("innings.sixes", Int::class.java).`as`("sixes"),
                field("innings.fivefor", Int::class.java).`as`("fivefor"),
                field("innings.tenfor", Int::class.java).`as`("tenfor"),
                field("innings.bbi", Double::class.java).`as`("bbi"),
                field("innings.bbm", Double::class.java).`as`("bbm"),
                coalesce(field("${teamsTempName}.teams", String::class.java)).`as`("teams"),
                coalesce(field("O.name", String::class.java)).`as`("opponents"),
                iif(
                    field("innings.wickets", Int::class.java).eq(0),
                    0, average
                ).`as`("avg"),

                iif(field("innings.balls", Int::class.java).eq(0), 0, economy).`as`("rpo"),
                iif(field("innings.wickets", Int::class.java).eq(0), 0, strikeRate).`as`("sr"),

                iif(
                    field("innings.wickets", Int::class.java).eq(0)
                        .or(field("innings.balls").eq(0)),
                    0, bi
                ).`as`("bi"),
                field("year")
            ).from(
                select(
                    field("${tempMatchCountsName}.count").`as`("matches"),
                    sum(field("didbowl", Int::class.java)).`as`("innings"),
                    field("${bowlTempName}.playerId"),
                    field("matchType"),
                    sum(field("balls", Int::class.java)).`as`("balls"),
                    sum(field("maidens", Int::class.java)).`as`("maidens"),
                    sum(field("wickets", Int::class.java)).`as`("wickets"),
                    sum(field("runs", Int::class.java)).`as`("runs"),
                    sum(field("wides", Int::class.java)).`as`("wides"),
                    sum(field("noballs", Int::class.java)).`as`("noballs"),
                    sum(field("fours", Int::class.java)).`as`("fours"),
                    sum(field("sixes", Int::class.java)).`as`("sixes"),
                    sum(field("dots", Int::class.java)).`as`("dots"),
                    sum(
                        `when`(
                            field("wickets", Int::class.java).ge(searchParameters.fivesLimit), 1
                        )
                    ).`as`("fivefor"),
                    trunc(sum(field("tenfor", Int::class.java)).div(2), 0).`as`("tenfor"),
                    max(field("syntheticBestBowling", Int::class.java)).`as`("bbi"),
                    max(field("syntheticBestBowlingMatch", Int::class.java)).`as`("bbm"),
                    field("${bowlTempName}.SeriesDate", String::class.java).`as`("year"),
                ).from(bowlTempName)
                    .join(tempMatchCountsName).on(
                        field("${tempMatchCountsName}.playerid", Int::class.java).eq(
                            field(
                                "${bowlTempName}.playerid",
                                Int::class.java
                            )
                        )
                    )
                    .and(field("${tempMatchCountsName}.SeriesDate", Int::class.java).eq(field("${bowlTempName}.SeriesDate", Int::class.java)))
                    .groupBy(field("matchtype"), field("playerid"), field("${bowlTempName}.SeriesDate"))
                    .having(sum(field("didbowl", Int::class.java)).cast(Int::class.java).gt(0))
                    .asTable("innings")
                    .join(PLAYERS).on(PLAYERS.ID.eq(field("innings.playerid", Int::class.java)))
                    .join(PLAYERSDATES).on(PLAYERSDATES.PLAYERID.eq(field("innings.playerid", Int::class.java)))
                    .and(PLAYERSDATES.MATCHTYPE.eq(searchParameters.matchType.value))
                    .leftJoin(teamsTempName)
                    .on(
                        field("${teamsTempName}.playerId", Int::class.java)
                            .eq(
                                field("innings.playerid", Int::class.java)
                            )
                    ).and(field("${teamsTempName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                    .and(field("${teamsTempName}.seriesDate", Int::class.java).eq(field("innings.year", Int::class.java)))
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId.id))
                    // todo: pass across the correct limit
                    .where(coalesce(field("innings.wickets").ge(searchParameters.pagingParameters.limit)))
            )

        return cte
    }

}