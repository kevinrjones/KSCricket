package com.knowledgespike.feature.bowlingrecords.data.repository


import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record
import org.jooq.SelectOnConditionStep
import org.jooq.impl.DSL.*

/**
 * The `JooqBowlingMatchTotals` object generates complex database queries
 * using jOOQ DSL for retrieving and aggregating bowling match statistics.
 * It is specifically designed to handle filtered and grouped cricket match
 * datasets based on a wide variety of input conditions.
 */
object JooqBowlingMatchTotals {

    /**
     * Creates a Common Table Expression (CTE) query object that represents filtered and aggregated bowling match results
     * based on the provided search parameters and the temporary table containing bowling details.
     *
     * This function applies various filtering conditions such as match result, venue, teams involved, date range or season,
     * specific ground, and host country, as well as performing aggregations and computations on the player's bowling
     * performance for the matches matching the specified criteria.
     *
     * @param searchParameters A set of validated criteria for filtering match results, including team, opponent, venue,
     *        result, date range, and more.
     * @param tmpBowlingDetailsName The name of the temporary table that stores detailed bowling statistics.
     *        Used to reference table data fields during the query.
     * @return A DSL query step representing the structured CTE for filtered and aggregated match results.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpBowlingDetailsName: String,
    ): SelectOnConditionStep<Record> {

        val searchCondition = buildSearchConditions(searchParameters)

        val cte =
            select(
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                PLAYERS.ID.`as`("playerid"),
                field("position", Int::class.java),
                rowNumber().over().partitionBy(field("MatchId"), field("playerid"))
                    .orderBy(field("MatchId"), field("playerid")).`as`("rn"),
                coalesce(
                    sum(
                        field("Balls", Int::class.java)
                    ).over().partitionBy(field("MatchId"), field("playerid"))
                        .orderBy(field("MatchId"), field("playerid")), 0
                ).`as`("Balls"),
                coalesce(
                    sum(field("Maidens", Int::class.java)).over().partitionBy(field("MatchId"), field("playerid"))
                        .orderBy(field("MatchId"), field("playerid")), 0
                ).`as`("Maidens"),
                coalesce(
                    sum(
                        field("Dots", Int::class.java)
                    ).over().partitionBy(field("MatchId"), field("playerid"))
                        .orderBy(field("MatchId"), field("playerid")), 0
                ).`as`("Dots"),
                coalesce(
                    sum(field("Runs", Int::class.java)).over().partitionBy(field("MatchId"), field("playerid"))
                        .orderBy(field("MatchId"), field("playerid")), 0
                ).`as`("Runs"),
                coalesce(
                    sum(field("Wickets", Int::class.java)).over().partitionBy(field("MatchId"), field("playerid"))
                        .orderBy(field("MatchId"), field("playerid")), 0
                ).`as`("Wickets"),
                field("SyntheticBestBowlingMatch", Int::class.java),
                field("captain", Int::class.java),
                field("inningsOrder", Int::class.java),
                field("inningsNumber", String::class.java),
                field("T.Name", String::class.java).`as`("teams"),
                field("O.name", String::class.java).`as`("opponents"),
                MATCHES.CAID.`as`("matchId"),
                MATCHES.LOCATIONID,
                MATCHES.MATCHDESIGNATOR,
                MATCHES.MATCHSTARTDATE,
                MATCHES.MATCHSTARTDATEASOFFSET,
                MATCHES.BALLSPEROVER,
                GROUNDS.KNOWNAS,
                iif(
                    field("balls", Int::class.java).eq(0),
                    0,
                    trunc(
                        field("runs", Int::class.java).div(cast(field("balls", Int::class.java), Double::class.java))
                            .mul(6), 2
                    ),
                ).`as`("rpo")
            ).from(tmpBowlingDetailsName)
                .join(MATCHES).on(MATCHES.ID.eq(field("${tmpBowlingDetailsName}.matchid", Int::class.java)))
                .join(EXTRAMATCHDETAILS)
                .on(
                    EXTRAMATCHDETAILS.MATCHID.eq(
                        field(
                            "${tmpBowlingDetailsName}.matchid",
                            Int::class.java
                        )
                    )
                )
                .and(
                    EXTRAMATCHDETAILS.TEAMID.eq(
                        field(
                            "${tmpBowlingDetailsName}.teamid",
                            Int::class.java
                        )
                    )
                )
                .and(searchCondition)
                .join(TEAMS.`as`("T")).on(field("T.id").eq(field("${tmpBowlingDetailsName}.TeamId")))
                .join(TEAMS.`as`("O")).on(field("O.id").eq(field("${tmpBowlingDetailsName}.OpponentsId")))
                .join(PLAYERS).on(PLAYERS.ID.eq(field("${tmpBowlingDetailsName}.PlayerId", Int::class.java)))
                .join(GROUNDS).on(GROUNDS.ID.eq(field("${tmpBowlingDetailsName}.locationId", Int::class.java)))


        return cte
    }
}