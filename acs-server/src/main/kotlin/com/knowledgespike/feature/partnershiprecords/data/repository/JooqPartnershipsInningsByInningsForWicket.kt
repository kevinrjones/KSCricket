package com.knowledgespike.feature.partnershiprecords.data.repository

import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import com.knowledgespike.jooq.buildSearchConditions
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.TableField
import org.jooq.impl.DSL.*
import org.jooq.impl.UpdatableRecordImpl

/**
 * Represents data operations for generating innings-by-innings partnership records based on wickets.
 *
 * This object contains methods to query partnerships data for cricket matches using JOOQ. The queries
 * leverage validated parameters to dynamically construct conditions and filters for retrieving partnership
 * statistics within specific search criteria.
 */
object JooqPartnershipsInningsByInningsForWicket {

    /**
     * Creates a Common Table Expression (CTE) that produces a filtered and processed result set
     * based on the provided validated search parameters. The resulting query captures various
     * statistics and metadata information for partnerships in cricket matches.
     *
     * The method applies numerous filtering and processing conditions using the search parameters,
     * including match details, team identifiers, opposition identifiers, venue and ground conditions,
     * date or season filters, result conditions, and specific partnership criteria.
     *
     * @param searchParameters The set of validated parameters used to filter and refine the CTE results.
     *                         This includes match type, teams, venue, date range, season, and additional
     *                         filtering, paging, and sorting preferences.
     * @return A `SelectConditionStep<Record>` representing the constructed query for the CTE results.
     *         The query can be used to fetch partnership statistics and related metadata for cricket matches.
     */
    fun createResultsCte(
        searchParameters: ValidatedSearchParameters
    ): SelectConditionStep<Record> {

        val cteName = "cteResults"


        val searchCondition = buildSearchConditions(searchParameters)

        val partitionBy: List<TableField<out UpdatableRecordImpl<*>, out Comparable<*>?>> =
            listOf(MATCHES.ID, PARTNERSHIPS.INNINGS, PARTNERSHIPS.WICKET, PARTNERSHIPS.TEAMID, PARTNERSHIPS.OPPONENTSID, PARTNERSHIPS.PLAYERIDS)

        val initialSelect = initialSelect(searchParameters, searchCondition, partitionBy)

        val cte =
            with(cteName).`as`(
                initialSelect
                    .and(PARTNERSHIPS.WICKET.eq(searchParameters.partnershipWicket))
                    .and(PARTNERSHIPS.PARTNERSHIP.ge(searchParameters.pagingParameters.limit))
            ).select().from(cteName)
                .where(field("${cteName}.rn").eq(1))

        return cte
    }


}