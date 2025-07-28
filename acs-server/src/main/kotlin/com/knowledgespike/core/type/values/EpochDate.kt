package com.knowledgespike.core.type.values

import arrow.core.raise.Raise
import com.knowledgespike.core.type.error.DateTimeError
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.NullError
import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

private const val SECONDS_IN_A_DAY = 86400
private const val BLANK_DATE_ERROR = "Epoch date cannot be constructed from a blank value"


/**
 * Represents a value class for handling dates as epoch seconds in a type-safe manner.
 * It ensures that dates are internally represented as a Long value of epoch seconds.
 */
@JvmInline
value class EpochDate(val value: Long) {


    /**
     * Private secondary constructor for creating an instance of `EpochDate` from a `LocalDate`.
     *
     * This constructor transforms the provided `LocalDate` into epoch seconds by assuming
     * the time as the start of the day (00:00:00) in the UTC time zone. The resultant epoch seconds
     * are passed to the primary constructor of `EpochDate`.
     *
     * @param date The `LocalDate` to be converted into epoch seconds.
     */
    private constructor(date: LocalDate) : this(
        LocalDateTime(date, LocalTime(0, 0, 0)).toInstant(TimeZone.UTC).epochSeconds
    )

    companion object {
        fun minimum() : EpochDate =
            EpochDate(-8520336000)

        /**
         * Converts a date string into an `EpochDate` based on the specified date format.
         * If the provided `dateString` is blank, it raises a `NullError`.
         * If the date parsing fails, it raises a `DateTimeError`.
         *
         * @param dateString The string representation of the date to be converted.
         * @param dateFormat The format in which the given date string is written. Defaults to "dd MMMM yyyy".
         * @return An `EpochDate` object representing the parsed date in epoch seconds.
         */
        context(Raise<Error>)
        operator fun invoke(dateString: String, dateFormat: String = "dd MMMM yyyy"): EpochDate {
            if (dateString.isBlank()) raise(NullError(BLANK_DATE_ERROR))
            return parseDate(dateString, dateFormat).let { EpochDate(it) }
        }

        /**
         * Parses a date string into a `LocalDate` object based on the specified date format.
         * If the parsing fails, raises a `DateTimeError`.
         *
         * @param dateString The string representation of the date to be parsed.
         * @param dateFormat The pattern describing the date format in which the `dateString` is written.
         * @return The parsed `LocalDate` object corresponding to the provided `dateString`.
         */
        context(Raise<Error>)
        @OptIn(FormatStringsInDatetimeFormats::class)
        private fun parseDate(dateString: String, dateFormat: String): LocalDate {
            val formatter = LocalDate.Format { byUnicodePattern(dateFormat) }
            return try {
                LocalDate.parse(dateString, formatter)
            } catch (e: Exception) {
                raise(DateTimeError("Date $dateString cannot be turned into an EpochDate"))
            }
        }
    }

    /**
     * Converts the `EpochDate` object to its epoch seconds representation.
     *
     * @return The number of seconds since the Unix epoch as a `Long`.
     */
    fun toLong(): Long = value

    /**
     * Converts the `EpochDate` to a `LocalDate`.
     *
     * This function interprets the `EpochDate`'s internal epoch time as the number of seconds
     * since the Unix epoch and converts it into a `LocalDate`.
     *
     * @return A `LocalDate` representation of the `EpochDate`.
     */
    fun toDateTime(): LocalDate = LocalDate.fromEpochDays((value / SECONDS_IN_A_DAY).toInt())
}