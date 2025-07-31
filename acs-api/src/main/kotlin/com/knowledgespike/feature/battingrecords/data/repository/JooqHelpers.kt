package com.knowledgespike.feature.battingrecords.data.repository

import org.jooq.Condition
import org.jooq.Field
import org.jooq.impl.DSL.*


/**
 * A predefined list of dismissal types that should be ignored during processing.
 * The values in the list represent specific dismissal type identifiers.
 */
val DISMISSAL_TYPES_TO_IGNORE = listOf(14, 16)

/**
 * Determines if the given field and type correspond to an excluded dismissal type.
 *
 * @param fieldName the name of the field in the database or query being evaluated
 * @param type the class type of the field to be evaluated
 * @return a condition that represents whether the specified field belongs to the excluded dismissal types
 */
fun <T : Any?> isExcludedDismissalType(fieldName: String, type: Class<T>): Condition {
    return field(fieldName, type).`in`(DISMISSAL_TYPES_TO_IGNORE)
}


/**
 * Computes a derived "balls" field that conditionally determines its value based on specific dismissal types.
 *
 * The method excludes certain dismissal types when evaluating the total number of balls.
 * If the count of excluded dismissals is such that the total balls need to be adjusted,
 * the result will be `null`. Otherwise, it calculates the sum of all balls.
 *
 * @return A field representing the computed value for "balls", adjusted to exclude specific dismissal types.
 */
fun computedBalls(): Field<*> {
    val ballsField = field("balls", Int::class.java)
    val dismissalTypeField = "dismissaltype"

    val excludedDismissalsCount = count(isExcludedDismissalType(dismissalTypeField, Int::class.java))
    val totalBallsCondition = count(ballsField).lt(count() - excludedDismissalsCount)

    return iif(
        totalBallsCondition,
        inline(null as Int?),
        sum(ballsField)
    ).`as`("balls")
}


fun buildLimitClause(fieldToCompare: Field<Any>, limit: Int): Field<Boolean> {
    return if (limit == 0) {
        trueCondition()
    } else {
        coalesce(fieldToCompare.ge(limit), inline(0))
    }
}





