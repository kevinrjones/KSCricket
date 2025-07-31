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
object JooqBowlingIndividualYearOfMatchStart {

    /**
     * Creates a Common Table Expression (CTE) query for retrieving batting statistics based on the provided
     * search criteria and temporary table names for intermediate calculations.
     *
     * The method constructs a SQL query that calculates various bowler performance metrics such as strike rate,
     * economy, average, balls bowled, runs given, wickets taken, and other statistics by joining data from
     * multiple tables and applying filters and groupings as defined by the input parameters.
     * It leverages the jOOQ DSL to dynamically build and return the query structure.
     *
     * @param searchParameters Contains the validated parameters required to filter and group the query results.
     *                         Includes match type, sorting, limits, and other search criteria.
     * @param bowlTempName The name of the temporary table containing bowling statistics data.
     * @param tempTeamsName The name of the temporary table containing team-related statistics data.
     * @param tempMatchCountsName The name of the temporary table containing match count data.
     * @return A `SelectJoinStep<Record>` representing the dynamically constructed SQL query for fetching bowler statistics.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        bowlTempName: String,
        tempTeamsName: String,
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
                coalesce(field("${tempTeamsName}.teams", String::class.java)).`as`("teams"),
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
                    field("${bowlTempName}.MatchStartYear", String::class.java).`as`("year"),
                ).from(bowlTempName)
                    .join(tempMatchCountsName)
                    .on(field("${tempMatchCountsName}.playerid", Int::class.java).eq(field("${bowlTempName}.playerid", Int::class.java)))
                    .and(field("${tempMatchCountsName}.MatchStartYear", Int::class.java).eq(field("${bowlTempName}.MatchStartYear", Int::class.java)))
                    .groupBy(field("matchtype"), field("playerid"), field("${bowlTempName}.MatchStartYear"))
                    .having(sum(field("didbowl", Int::class.java)).cast(Int::class.java).gt(0))
                    .asTable("innings")
                    .join(PLAYERSDATES).on(PLAYERSDATES.PLAYERID.eq(field("innings.playerid", Int::class.java)))
                    .and(PLAYERSDATES.MATCHTYPE.eq(searchParameters.matchType.value))
                    .join(PLAYERS).on(PLAYERS.ID.eq(field("innings.playerid", Int::class.java)))
                    .leftJoin(tempTeamsName)
                    .on(
                        field("${tempTeamsName}.playerId", Int::class.java)
                            .eq(
                                field("innings.playerid", Int::class.java)
                            )
                    )
                    .and(field("${tempTeamsName}.matchType", String::class.java).eq(searchParameters.matchType.value))
                    .and(field("${tempTeamsName}.matchstartyear", Int::class.java).eq(field("innings.year", Int::class.java)))
                    .leftJoin(TEAMS.`as`("O")).on(field("O.id").eq(searchParameters.opponentsId.id))
                    // todo: pass across the correct limit
                    .where(coalesce(field("innings.wickets").ge(searchParameters.pagingParameters.limit)))
            )

        return cte
    }

}