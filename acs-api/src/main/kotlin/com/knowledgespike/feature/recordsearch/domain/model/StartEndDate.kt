package com.knowledgespike.feature.recordsearch.domain.model

import com.knowledgespike.core.type.shared.now
import com.knowledgespike.core.type.shared.toSeconds
import com.knowledgespike.core.type.values.MatchType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.serialization.Serializable

/**
 * Represents a date range with a start and end timestamp.
 *
 * This data class is used to define a range of dates or timestamps,
 * typically for filtering or specifying a time period in search or query operations.
 *
 * @property start The starting timestamp of the date range, represented as a Long.
 * @property end The ending timestamp of the date range, represented as a Long.
 */
@Serializable
data class StartEndDate(val start: Long = 0, val end: Long = 0)

/**
 * Represents the date details of a match.
 *
 * This class is used to model the date information associated with a match,
 * including the specific date, date offset, and the type of match.
 *
 * @property date The specific date of the match in string format.
 * @property dateOffset The time offset (in milliseconds) associated with the match date.
 * @property matchType The type or classification of the match (e.g., test, ODI, T20).
 */
@Serializable
data class MatchDate(val date: String = "", val dateOffset: Long = 0, val matchType: String = "")

/**
 * Returns the minimum `MatchDate` object with pre-defined values.
 *
 * This method defines a constant `MatchDate` object representing
 * the minimum allowable date, using `1772-06-24` as the initialized date
 * and a `dateOffset` computed from the epoch representation of `0001-01-01`.
 * The `matchType` is set to the default match type value.
 *
 * @return A `MatchDate` object representing the minimum allowable date configuration.
 */
fun MatchDate.Companion.minimum(): MatchDate {
    val localDate = LocalDate.parse("0001-01-01")
    return MatchDate(date = "1772-06-24", localDate.toSeconds(), MatchType.default().value)
}

/**
 * Provides the maximum `MatchDate` value based on the current date and time.
 *
 * Utilizes the current system's date and time to generate a `MatchDate` object
 * with the current ISO date string, the corresponding epoch seconds, and
 * the default match type value.
 *
 * @return A `MatchDate` object representing the maximum state, constructed
 *         with the current date, time offset in seconds, and a default match type.
 */
fun MatchDate.Companion.maximum(): MatchDate {
    val formatter = LocalDate.Formats.ISO
    val now = LocalDate.now()
    now.format(formatter)
    return MatchDate(now.format(formatter), now.toSeconds(), MatchType.default().value)
}

/**
 * A formatting template for representing dates with a full month name.
 *
 * This variable defines a custom date format that includes the day of the month,
 * a space character, the full name of the month (in English), a comma and space,
 * followed by the year. It utilizes `LocalDate.Format` for specifying the structure.
 */
val formatFullMonths = LocalDate.Format {
    dayOfMonth()
    char(' ')
    monthName(MonthNames.ENGLISH_FULL)
    chars(", ")
    year()
}