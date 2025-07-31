package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.INNINGS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.DSLContext
import org.jooq.Record16
import org.jooq.Record17
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import org.jooq.impl.SQLDataType.DECIMAL
import java.math.BigDecimal

/**
 * An object containing methods to compute and manage cricket team career statistics
 * by utilizing temporary tables and CTEs for filtering and aggregation.
 * This object typically interacts with a database using DSLContext to generate SQL queries
 * for gaining insights into team performances.
 */
object JooqTeamBySeason {

    /**
     * Creates a temporary table to store filtered match scores based on the provided search parameters.
     *
     * The method generates a temporary table with scores filtered by various conditions, such as match type,
     * team IDs, venue, date, season, and other criteria defined in the `searchParameters`. The table is
     * created in the database specified by the `context` and is named as per the `tempTableName`.
     *
     * @param context The DSLContext used to execute database operations.
     * @param tempTableName The name of the temporary table to be created.
     * @param searchParameters An instance of `ValidatedSearchParameters` containing the filtering criteria for creating the scores table.
     */
    fun createTemporaryScores(
        context: DSLContext,
        tempTableName: String,
        searchParameters: ValidatedSearchParameters,
    ) {


        val searchCondition = buildSearchConditions(searchParameters)

        context.dropTableIfExists(tempTableName).execute()
        context.createTemporaryTableIfNotExists(tempTableName).`as`(
            select(
                MATCHES.ID,
                EXTRAMATCHDETAILS.TEAMID,
                EXTRAMATCHDETAILS.OPPONENTSID,
                EXTRAMATCHDETAILS.RESULT,
                MATCHES.MATCHTYPE,
                MATCHES.SERIESDATE,
            ).from(MATCHES).join(EXTRAMATCHDETAILS).on(MATCHES.ID.eq(EXTRAMATCHDETAILS.MATCHID))
                .where(EXTRAMATCHDETAILS.MATCHTYPE.eq(searchParameters.matchType.value))
                .and(searchCondition)
                .and(MATCHES.VICTORYTYPE.notIn(6, 11, 13))
        ).execute()
    }

    /**
     * Creates a temporary table summarizing team totals and performance statistics.
     *
     * @param context The DSLContext used to execute SQL queries.
     * @param tempTableName The name of the temporary table to be created.
     * @param scoresTempTableName The name of the source temporary table containing scores data.
     */
    fun createTotalsTable(
        context: DSLContext,
        tempTableName: String,
        scoresTempTableName: String,
        searchParameters: ValidatedSearchParameters,
    ) {

        val average = iif(
            sum(field("wickets", Int::class.java)).eq(BigDecimal(0)), inline(null, Int::class.java),
            trunc(sum(field("total", Int::class.java)).div(sum(field("wickets", Int::class.java))), 2)
        ).`as`("avg")

        val rpo = iif(
            sum(field("ballsBowled", Int::class.java)).eq(BigDecimal(0)), inline(null, Int::class.java),
            trunc(sum(field("total", Int::class.java)).div(sum(field("ballsBowled", Int::class.java)).div(6)), 2)
        ).`as`("rpo")

        val compareTeamId = if (searchParameters.isTeamBattingRecord)
            INNINGS.TEAMID
        else
            INNINGS.OPPONENTSID

        val groupByFields = JooqTeamHelpers.getGroupingFieldsForSearchParameters(scoresTempTableName, searchParameters, "seriesDate")

        context.dropTableIfExists(tempTableName).execute()
        context.createTemporaryTableIfNotExists(tempTableName).`as`(
            select(
                field("${scoresTempTableName}.teamId"),
                field("${scoresTempTableName}.opponentsId"),
                field("${scoresTempTableName}.seriesdate"),
                sum(field("didbat", Int::class.java)).`as`("innings"),
                sum(field("total", Int::class.java)).`as`("runs"),
                sum(field("ballsBowled", Int::class.java)).`as`("ballsBowled"),
                sum(field("wickets", Int::class.java)).`as`("wickets"),
                max(field("total", Int::class.java)).`as`("hs"),
                average,
                rpo
            ).from(scoresTempTableName)
                .join(MATCHES).on(field("${scoresTempTableName}.id").eq(MATCHES.ID))
                .join(INNINGS).on(field("${scoresTempTableName}.id").eq(INNINGS.MATCHID))
                .where(field("${scoresTempTableName}.teamid").eq(compareTeamId))
                .groupBy(groupByFields)
        ).execute()
    }

    /**
     * Constructs a Common Table Expression (CTE) query to calculate various statistics related to cricket game results,
     * including wins, losses, draw, tied matches, averages, strike rates, and other key metrics for each team.
     *
     * @param scoresTempTableName The name of the temporary table containing score data for processing.
     * @param totalTableName The name of the table containing aggregate data such as runs and balls played.
     * @param minimumValue The minimum value of total runs required to filter the results.
     * @return A jOOQ SelectConditionStep<Record15> object representing the CTE query with various calculated fields.
     */
    fun createResultsCte(
        scoresTempTableName: String,
        totalTableName: String,
        minimumValue: Int,
        isForBattingTeam: Boolean,
    ): SelectConditionStep<Record16<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, BigDecimal, Any>> {

        val strikeRate = iif(
            field("${totalTableName}.ballsBowled", Int::class.java).eq(0), cast(inline(0.0), DECIMAL(7, 2)),
            cast(
                field("${totalTableName}.runs", Int::class.java).div(
                    field(
                        "${totalTableName}.ballsbowled",
                        Double::class.java
                    )
                ).mul(100), DECIMAL(7, 2)
            )

        )

        val average = iif(
            field("${totalTableName}.ballsBowled", Int::class.java).eq(0),
            cast(inline(0.0, Double::class.java), DECIMAL(7, 2)),
            cast(field("runs", Int::class.java).div(sum(field("ballsBowled", Int::class.java))), DECIMAL(7, 2))
        ).`as`("avg")

        val compareTeamId = if (isForBattingTeam)
            INNINGS.TEAMID
        else
            INNINGS.OPPONENTSID


        val cte =
            with("cteResults").`as`(
                select(
                    field("seriesdate", Int::class.java),
                    field("teamId", Int::class.java),
//                    field("opponentsId", Int::class.java),
                    rowNumber().over().partitionBy(field("teamid"), field("seriesdate")).`as`("rn"),
                    sum(
                        `when`(
                            field("result").eq(1),
                            1
                        ).else_(0)
                    ).over().partitionBy(field("teamid"), field("seriesdate")).`as`("won"),
                    sum(
                        `when`(
                            field("result").eq(2),
                            1
                        ).else_(0)
                    ).over().partitionBy(field("teamid"), field("seriesdate")).`as`("lost"),
                    sum(
                        `when`(
                            field("result").eq(4),
                            1
                        ).else_(0)
                    ).over().partitionBy(field("teamid"), field("seriesdate")).`as`("drawn"),
                    sum(
                        `when`(
                            field("result").eq(8),
                            1
                        ).else_(0)
                    ).over().partitionBy(field("teamid"), field("seriesdate")).`as`("tied"),
                )
                    .from(scoresTempTableName)
            )
                .with("cteLowestScore").`as`(
                    select(
                        field("${scoresTempTableName}.teamid", Int::class.java),
                        field("${scoresTempTableName}.seriesdate", Int::class.java),
                        min(field("total", Int::class.java)).`as`("ls")
                    ).from(scoresTempTableName)
                        .join(INNINGS).on(field("${scoresTempTableName}.id").eq(INNINGS.MATCHID))
                        .where(field("${scoresTempTableName}.teamid").eq(compareTeamId))
                        .and(INNINGS.COMPLETE.eq(1))
                        .groupBy(field("${scoresTempTableName}.seriesdate"), field("${scoresTempTableName}.teamid"))
                )
                .select(
                    field("T.Name").`as`("team"),
//                    field("O.Name").`as`("opponents"),
                    coalesce(field("games.played"), 0).`as`("played").`as`("matches"),
                    field("innings"),
                    coalesce(field("cteResults.won"), 0).`as`("won"),
                    coalesce(field("cteResults.lost"), 0).`as`("lost"),
                    coalesce(field("cteResults.tied"), 0).`as`("tied"),
                    coalesce(field("cteResults.drawn"), 0).`as`("drawn"),
                    coalesce(field("${totalTableName}.runs"), 0).`as`("Runs"),
                    coalesce(field("${totalTableName}.ballsBowled"), 0).`as`("totalBalls"),
                    coalesce(field("${totalTableName}.wickets"), 0).`as`("wickets"),
                    coalesce(field("${totalTableName}.avg"), 0).`as`("avg"),
                    coalesce(field("${totalTableName}.rpo"), 0).`as`("rpo"),
                    field("hs"),
                    field("ls"),
                    strikeRate.`as`("sr"),
                    field("${totalTableName}.seriesDate").`as`("seriesDate"),
                ).from(
                    select(
                        field("matchType", Int::class.java),
                        field("teamid", Int::class.java),
//                        field("opponentsId", Int::class.java),
                        field("seriesDate", Int::class.java),
                        count().`as`("played")
                    )
                        .from(scoresTempTableName)
                        .groupBy(
                            field("seriesDate"),
                            field("teamid"),
                            field("matchType"),
//                            field("opponentsId"),
                        ).asTable("games")
                        .leftJoin(totalTableName).on(field("games.teamid").eq(field("${totalTableName}.teamid")))
                        .and(field("games.seriesdate").eq(field("${totalTableName}.seriesdate")))

                        .leftJoin("cteResults")
                        .on(field("games.teamid").eq(field("cteResults.teamid")))
                        .and(field("games.seriesdate").eq(field("cteResults.seriesdate")))
                        .and(field("rn").eq(1))

                        .leftJoin("cteLowestScore")
                        .on(field("cteLowestScore.teamid").eq(field("${totalTableName}.teamid")))
                        .and(field("cteLowestScore.seriesdate").eq(field("${totalTableName}.seriesdate")))

                        .join(TEAMS.`as`("T")).on(field("games.teamid").eq(field("T.id")))
//                        .join(TEAMS.`as`("O")).on(
//                            field("games.OpponentsId").eq(field("O.id")))
                ).where(field("${totalTableName}.runs").ge(minimumValue))

        return cte
    }


}