package com.knowledgespike.core.type.values

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.right
import com.knowledgespike.core.type.error.MatchTypeError
import com.knowledgespike.core.type.shared.MatchTypes
import com.knowledgespike.core.type.shared.MatchTypes.validMatchTypes
import com.knowledgespike.feature.recordsearch.domain.model.MatchTypeEntity

/**
 * Represents a specific type of match within a system.
 *
 * This class is implemented as a value class, ensuring efficient handling
 * while maintaining type safety. It encapsulates a predefined valid match type as a string.
 *
 * The `MatchType` class includes methods to check specific characteristics
 * of the match type, such as whether it represents a multi-innings match or
 * an international match. Additionally, it provides functionality to ensure
 * that only valid match types can be created, leveraging the `Raise` context for error handling.
 *
 * @property value A string representing the match type.
 *
 * Methods:
 * - `isMultiInningsType`: Determines if the match type is a multi-innings type.
 * - `isInternationalType`: Determines if the match type is an international type.
 *
 * Companion Object:
 * - Provides factory methods to create and validate `MatchType` instances.
 *   Includes a default match type and a way to handle invalid match types using `Raise`.
 */
@JvmInline
value class MatchType private constructor(val value: String) {

    fun isMultiInningsType(): Boolean {
        return MatchTypes.multiInningsMatchTypes.contains(this.value)
    }

    fun isInternationalType(): Boolean {
        return MatchTypes.internationalMatchTypes.contains(this.value)
    }

    companion object {

        context(Raise<MatchTypeError>)
        operator fun invoke(value: String?): MatchType {
            if (value != null && validMatchTypes.contains(value))
                return MatchType(value)
            raise(MatchTypeError("The 'matchType' $value is not a valid match type", value ?: "null"))
        }

        fun default(): MatchType = MatchType("t")
    }

    override fun toString(): String = value
}


context(Raise<MatchTypeError>)
fun String.toMatchType(): Either<MatchTypeError, MatchType> = MatchType(this).right()

context(Raise<MatchTypeError>)
fun MatchTypeEntity.toMatchType(): Either<MatchTypeError, MatchType> = MatchType(this.type).right()

