package com.knowledgespike.feature.fieldingrecords.data.repository

import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.MATCHSUBTYPE
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record19
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*
import java.time.LocalDate

/**
 * Object that provides methods to handle and query fielding statistics on a per-innings basis.
 * This is designed to interact with a database schema using jOOQ, particularly for generating
 * SQL queries related to fielding performance in cricket matches.
 */
object JooqFieldingInningsByInnings {

    /**
     * Creates a Common Table Expression (CTE) that constructs a query for fetching player and match-related statistics
     * based on the provided validated search parameters and temporary fielding details table name. The method applies
     * various filters to the query, such as team, opponents, venue, date/season, match result, and other parameters.
     *
     * @param searchParameters A set of validated search parameters that specify match types, team IDs, opponents,
     *        date ranges, and other filtering and sorting criteria.
     * @param tmpFieldingDetailsName The name of the temporary table containing fielding details for processing match
     *        and player data.
     * @return A `SelectConditionStep<Record19>` that represents the constructed query, including fields for player information,
     *         match details, and calculated statistics such as dismissals and wicket-keeping records.
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
            field("InningsNumber", Int::class.java).`as`("inningsOrder"),
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
            .join(TEAMS.`as`("T")).on(field("T.id").eq(field("${tmpFieldingDetailsName}.TeamId")))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(field("${tmpFieldingDetailsName}.OpponentsId")))
            .join(PLAYERS).on(PLAYERS.ID.eq(field("${tmpFieldingDetailsName}.PlayerId", Int::class.java)))
            .join(GROUNDS).on(GROUNDS.ID.eq(field("${tmpFieldingDetailsName}.locationId", Int::class.java)))
            .and(MATCHES.MATCHTYPE.eq(searchParameters.matchType.value))
            .and(searchCondition)
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