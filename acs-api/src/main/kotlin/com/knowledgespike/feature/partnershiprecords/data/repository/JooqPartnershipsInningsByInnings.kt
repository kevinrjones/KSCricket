package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.*

/**
 * Provides methods to construct SQL queries for partnerships data on an innings-by-innings basis.
 * This object uses jOOQ DSL to create queries for partnerships based on various filtering criteria.
 */
object JooqPartnershipsInningsByInnings {

    /**
     * Creates a Common Table Expression (CTE) query for fetching partnership results
     * based on the provided validated search parameters. The CTE applies various conditions
     * to filter data related to matches, partnerships, and associated entities like teams,
     * grounds, and players.
     *
     * @param searchParameters A validated set of search parameters that includes match types,
     * team identifiers, venue details, date ranges, seasons, and additional filtering or sorting options.
     * @return A `SelectConditionStep<Record>` object representing the built query that selects
     * relevant partnership results and their associated data.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record> {

        val cteName = "cteResults"


        val searchCondition = buildSearchConditions(searchParameters)

        val partitionBy = listOf(MATCHES.ID, PARTNERSHIPS.INNINGS, PARTNERSHIPS.WICKET, PARTNERSHIPS.TEAMID, PARTNERSHIPS.OPPONENTSID, PARTNERSHIPS.PLAYERIDS)

        val initialSelect = initialSelect(searchParameters, searchCondition, partitionBy)

        val cte =
            with(cteName).`as`(
                initialSelect
            ).select().from(cteName)
                .where(field("${cteName}.rn").eq(1))
                .and(field("${cteName}.runs").ge(searchParameters.pagingParameters.limit))

        return cte
    }


}