package com.knowledgespike.feature.scorecard.domain.helpers

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.UrlError
import java.net.URLDecoder

/**
 * Represents a template for searching scorecards by combining home team, away team, and date.
 * Instances of this class are created using the `create` function, which validates and parses
 * a predefined string format.
 *
 * @constructor Private constructor to prevent direct instantiation. Use the `create` function instead.
 * @property homeTeam The name of the home team.
 * @property awayTeam The name of the away team.
 * @property date The date of the match in a specific format.
 */
class ScorecardSearchTemplate private constructor(
    var homeTeam: String,
    var awayTeam: String,
    var date: String
) {

    /**
     * Companion object associated with the `ScorecardSearchTemplate` class, providing factory
     * methods and utilities to create and manage `ScorecardSearchTemplate` instances.
     */
    companion object {
        /**
         * Creates a `ScorecardSearchTemplate` instance from the provided string value.
         *
         * The input string must follow the format `name-v-name-date`, where:
         * - `name` represents a team name.
         * - `v` is a fixed literal indicating a match.
         * - `date` represents the match date.
         *
         * If the input string is not in the expected format, an `UrlError` is returned.
         *
         * @param value The input string used to parse and create a `ScorecardSearchTemplate`.
         * @return An `Either` type containing a `ScorecardSearchTemplate` on success,
         *         or an `Error` (specifically `UrlError`) if parsing fails.
         */
        fun create(value: String): Either<Error, ScorecardSearchTemplate> {
            // name-v-name-date
            val values = value.split("-")
            if (values.size != 4 || values[1] != "v") {
                return UrlError("The template is not in the correct format, the format should be 'name-v-name-date' where name and date are values, 'v' and '-' are constants").left()

            }

            return ScorecardSearchTemplate(
                URLDecoder.decode(values[0], "UTF-8"),
                URLDecoder.decode(values[2], "UTF-8"),
                URLDecoder.decode(values[3], "UTF-8")
            ).right()
        }
    }

}