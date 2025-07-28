package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.DSLContext
import org.jooq.Record19
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.time.LocalDate

/**
 * Represents a utility object for managing operations related to fielding match totals.
 * Provides methods for creating temporary tables and generating Common Table Expressions (CTEs)
 * for fielding data analysis.
 */
object JooqFieldingMatchTotals {

    /**
     * Creates a temporary table for fielding details by joining and processing fielding data
     * and match details based on the provided table names and match conditions.
     *
     * @param context the DSLContext used for interacting with the database.
     * @param fieldingDetailsTempTableName the name of the temporary table to be created for fielding details.
     * @param matchIdTempTableName the name of the temporary table containing match IDs to filter data.
     */
    fun createTemporaryFieldingDetailsTable(
        context: DSLContext,
        fieldingDetailsTempTableName: String,
        matchIdTempTableName: String
    ) {
        context.dropTableIfExists(fieldingDetailsTempTableName).execute()
        context.createTemporaryTableIfNotExists(fieldingDetailsTempTableName).`as`(
            select(
                field("f1.playerid", Int::class.java),
                field("f1.dismissals", Int::class.java)
                    .add(field("f2.dismissals", Int::class.java)).`as`("dismissals"),
                field("f1.caughtwk", Int::class.java)
                    .add(field("f1.stumped", Int::class.java))
                    .add(field("f2.caughtwk", Int::class.java))
                    .add(field("f2.stumped", Int::class.java))
                    .`as`("wicketKeeperDismissals"),
                field("f1.caughtwk", Int::class.java)
                    .add(field("f1.caughtf", Int::class.java))
                    .add(field("f2.caughtwk", Int::class.java))
                    .add(field("f2.caughtf", Int::class.java))
                    .`as`("caught"),
                field("f1.stumped", Int::class.java)
                    .add(field("f2.stumped", Int::class.java))
                    .`as`("stumped"),
                field("f1.caughtwk", Int::class.java)
                    .add(field("f2.caughtwk", Int::class.java))
                    .`as`("caughtwk"),
                field("f1.caughtf", Int::class.java)
                    .add(field("f2.caughtf", Int::class.java))
                    .`as`("caughtf"),
                field("f1.InningsNumber", Int::class.java).`as`("inningsOrder"),
                field("f1.MatchId", Int::class.java).`as`("MatchId"),
                field("f1.TeamId", Int::class.java).`as`("TeamId"),
                field("f1.OpponentsId", Int::class.java).`as`("OpponentsId"),
                MATCHES.LOCATIONID,
            ).from(FIELDING.`as`("f1"))
                .leftJoin(FIELDING.`as`("f2"))
                .on(field("f1.matchId").eq(field("f2.matchId")))
                .and(field("f1.playerId").eq(field("f2.playerId")))
                .join(MATCHES).on(MATCHES.ID.eq(field("f1.matchId", Int::class.java)))
                .where(
                    field("f1.MATCHID").`in`(
                        select(
                            field("id", Int::class.java)
                        ).from(matchIdTempTableName)
                    ).and(
                        field("f1.PLAYERID").ne(1)
                    )
                )
                .and(field("f1.inningsNumber").eq(1))
                .and(field("f2.inningsNumber").eq(2))
        ).execute()
    }

    /**
     * Creates a Common Table Expression (CTE) for retrieving cricket match fielding statistics
     * based on the provided search parameters and fielding details table name.
     *
     * The method constructs a query using multiple conditions derived from the search parameters,
     * including filters for team, opponent, ground, date range, season, match results, and venue.
     * The resulting CTE includes detailed statistics such as player dismissals, wicket-keeping
     * dismissals, match details, and player information.
     *
     * @param searchParameters A validated set of search parameters containing filtering and
     *                          sorting information for the statistics query. This includes
     *                          parameters like match type, opponent team, venue, date range,
     *                          and paging configurations.
     * @param tmpFieldingDetailsName The name of the temporary fielding details table used in the query.
     * @return A SelectConditionStep<Record19> object representing the constructed CTE query,
     *         which retrieves fielding statistics based on the supplied search parameters.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters,
        tmpFieldingDetailsName: String,
    ): SelectConditionStep<Record19<String?, String?, Int, String, Int, Int, Int, Int, Int, Int, Int, String, String, String, String?, LocalDate?, Long?, String?, Int?>> {

        val searchCondition = buildSearchConditions(searchParameters)

        val cte = select(
            PLAYERS.FULLNAME,
            PLAYERS.SORTNAMEPART,
            field("players.Id", Int::class.java).`as`("playerid"),
            field("T.Name", String::class.java).`as`("teams"),
            field("dismissals", Int::class.java),
            field("caughtwk", Int::class.java).add(field("stumped", Int::class.java))
                .`as`("wicketKeeperDismissals"),
            field("caughtwk", Int::class.java).add(field("caughtf", Int::class.java)).`as`("caught"),
            field("stumped", Int::class.java).`as`("stumpings"),
            field("caughtwk", Int::class.java).`as`("caughtkeeper"),
            field("caughtf", Int::class.java).`as`("caughtfielder"),
            field("inningsOrder", Int::class.java),
            field("O.Name", String::class.java).`as`("opponents"),
            field("Matches.matchDesignator", String::class.java).`as`("matchDesignator"),
            field("Matches.matchStartDate", String::class.java).`as`("matchDate"),
            GROUNDS.KNOWNAS,
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.CAID,
            MATCHES.ID.`as`("matchId"),
        ).from(tmpFieldingDetailsName)
            .join(MATCHES).on(MATCHES.ID.eq(field("${tmpFieldingDetailsName}.matchid", Int::class.java)))
            .join(EXTRAMATCHDETAILS)
            .on(
                EXTRAMATCHDETAILS.MATCHID.eq(
                    field(
                        "${tmpFieldingDetailsName}.matchid",
                        Int::class.java
                    )
                )
            )
            .and(
                EXTRAMATCHDETAILS.TEAMID.eq(
                    field(
                        "${tmpFieldingDetailsName}.teamid",
                        Int::class.java
                    )
                )
            )
            .and(searchCondition)
            .join(TEAMS.`as`("T")).on(field("T.id").eq(field("${tmpFieldingDetailsName}.TeamId")))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(field("${tmpFieldingDetailsName}.OpponentsId")))
            .join(PLAYERS).on(PLAYERS.ID.eq(field("${tmpFieldingDetailsName}.PlayerId", Int::class.java)))
            .join(GROUNDS).on(GROUNDS.ID.eq(field("${tmpFieldingDetailsName}.locationId", Int::class.java)))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .where(coalesce(field("${tmpFieldingDetailsName}.DISMISSALS").ge(searchParameters.pagingParameters.limit)))
            .and(
                field("${tmpFieldingDetailsName}.matchid", Int::class.java)
                    .`in`(
                        select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                            .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
                    )
            )

        return cte
    }
}