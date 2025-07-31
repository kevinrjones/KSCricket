package com.knowledgespike.feature.teamrecords.data.repository

import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import org.jooq.Field
import org.jooq.impl.DSL.field

/**
 * Provides utility functions for handling team-based operations in conjunction with jOOQ.
 */
object JooqTeamHelpers {
    /**
     * Retrieves a list of grouping fields based on the provided search parameters.
     *
     * This function dynamically determines the fields to use for grouping data based
     * on the specified `scoresTempTableName`, `searchParameters`, and `fieldName`.
     * It considers conditions such as the presence of team and opponent identifiers in
     * the search parameters to construct the grouping fields.
     *
     * @param scoresTempTableName The name of the temporary table containing scores data.
     * @param searchParameters The validated search parameters specifying the query criteria.
     * @param fieldName The specific field name to be included in the grouping fields.
     * @return A list of grouping fields as instances of `Field<*>`.
     */
    fun getGroupingFieldsForSearchParameters(
        scoresTempTableName: String,
        searchParameters: ValidatedSearchParameters,
        fieldName: String,
    ): List<Field<*>> {
        return if (searchParameters.teamId.id != 0 && searchParameters.opponentsId.id != 0) {
            listOf(
                field("${scoresTempTableName}.teamid"),
                field("${scoresTempTableName}.opponentsid"),
                field("${scoresTempTableName}.${fieldName}"),
            )
        } else if (searchParameters.opponentsId.id != 0) {
            listOf(
                field("${scoresTempTableName}.teamid"),
                field("${scoresTempTableName}.opponentsid"),
                field("${scoresTempTableName}.${fieldName}"),
            )
        } else {
            listOf(
                field("${scoresTempTableName}.teamid"),
                field("${scoresTempTableName}.${fieldName}"),
            )
        }
    }

    /**
     * Calculates the number of overs based on the total number of balls and the balls per over.
     *
     * @param balls The total number of balls bowled.
     * @param ballsPerOver The number of balls in one over.
     * @return A string representing the number of overs in the format "completedOvers.ballsLeft".
     */
    fun getOvers(balls: Int, ballsPerOver: Int) : String {
        val completedOvers = balls/ballsPerOver
        val ballsLeft = balls % ballsPerOver
        return "$completedOvers.$ballsLeft"
    }

}