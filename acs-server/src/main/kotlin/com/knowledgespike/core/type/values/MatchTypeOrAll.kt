package com.knowledgespike.core.type.values

import arrow.core.raise.Raise
import com.knowledgespike.core.type.error.MatchTypeError
import com.knowledgespike.core.type.shared.MatchTypes
import com.knowledgespike.core.type.shared.MatchTypes.internationalMatchTypes
import com.knowledgespike.core.type.shared.MatchTypes.multiInningsMatchTypes

/**
 * Represents a value class for match type or the special value "all".
 * The class enforces type-safe operations on match types, ensuring that only valid match types
 * or the "all" value can be used. It includes capabilities to determine characteristics of the match type.
 *
 * @property value The string representation of the match type or the special value "all".
 */
@JvmInline
value class MatchTypeOrAll private constructor(val value: String) {

    /**
     * Determines whether the match type is a multi-innings type.
     *
     * This method checks if the value of the current instance exists in the `multiInningsMatchTypes` collection.
     *
     * @return `true` if the match type is considered a multi-innings type, otherwise `false`.
     */
    fun isMultiInningsType(): Boolean {
        return multiInningsMatchTypes.contains(this.value)
    }

    /**
     * Determines if the match type is classified as an international match type.
     *
     * @return `true` if the match type is an international type, otherwise `false`.
     */
    fun isInternationalType(): Boolean {
        return internationalMatchTypes.contains(this.value)
    }

    /**
     * Companion object for the `MatchTypeOrAll` value class.
     * Provides functionality for creating and working with instances of `MatchTypeOrAll`.
     */
    companion object {

        /**
         * Invokes a `MatchTypeOrAll` instance based on the provided string value.
         *
         * @param value The string representation of the match type. It should either be "all"
         *              or a valid match type contained within `MatchTypes.validMatchTypes`.
         *              If the value is invalid or null, a `MatchTypeError` is raised.
         * @return A `MatchTypeOrAll` instance encapsulating the provided match type or "all".
         */
        context(Raise<MatchTypeError>)
        operator fun invoke(value: String?): MatchTypeOrAll {
            if (value != null && (value == "all" || MatchTypes.validMatchTypes.contains(value)))
                return MatchTypeOrAll(value)
            raise(MatchTypeError("The 'matchType' $value is not a valid match type", value ?: "null"))
        }

        /**
         * Provides a default instance of `MatchTypeOrAll` with the value "all".
         *
         * @return A `MatchTypeOrAll` object initialized to "all".
         */
        fun default(): MatchTypeOrAll = MatchTypeOrAll("all")
    }

    /**
     * Returns the string representation of the MatchTypeOrAll instance.
     *
     * @return The string value of the MatchTypeOrAll instance.
     */
    override fun toString(): String = value
}