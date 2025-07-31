package com.knowledgespike.feature.recordsearch.data.repository

import arrow.core.Either
import arrow.core.right
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.db.tables.references.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.references.MATCHES
import com.knowledgespike.feature.recordsearch.domain.model.MatchListDto
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedMatchSearchParameters
import com.knowledgespike.feature.recordsearch.domain.model.formatFullMonths
import com.knowledgespike.feature.scorecard.domain.model.VictoryType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import org.jooq.DSLContext

/**
 * This object handles the process of searching for match records in the database
 * based on various validated match search parameters. It utilizes the Jooq library
 * for constructing and executing database queries in a type-safe manner.
 */
object JooqFindMatches {
    /**
     * Finds matches based on the given search parameters and returns either a list of matches
     * or an error if the search fails.
     *
     * @param context The DSL context to execute SQL queries.
     * @param parameters Validated parameters used for filtering the search results, including
     * various match criteria such as home team, away team, date range, and match type.
     * @return Either an error object if the search fails, or a list of matches wrapped in a
     * mutable list of MatchListDto.
     */
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun findMatches(
        context: DSLContext,
        parameters: ValidatedMatchSearchParameters
    ): Either<Error, MutableList<MatchListDto>> {

        val victoryType = getVictoryValuesFromMatchResult(parameters.matchResult)
        val venue = getVenue(parameters.venue)

        val likeHomeTeamName =
            if (parameters.homeTeamExactMatch) parameters.homeTeam else "%" + parameters.homeTeam + "%"

        val likeAwayTeamName =
            if (parameters.awayTeamExactMatch) parameters.awayTeam else "%" + parameters.awayTeam + "%"

        val matchresult = mutableListOf(1, 2, 4, 8)
        if (parameters.matchResult == 1
            || parameters.matchResult == 2
            || parameters.matchResult == 3
            || parameters.matchResult == 4
        ) {
            matchresult.clear()
            matchresult.add(1)
        }

        if (parameters.matchResult == 5
            || parameters.matchResult == 6
            || parameters.matchResult == 7
            || parameters.matchResult == 8
        ) {
            matchresult.clear()
            matchresult.add(2)
        }

        if (parameters.matchResult == 9) {
            matchresult.clear()
            matchresult.add(4)
        }

        if (parameters.matchResult == 10) {
            matchresult.clear()
            matchresult.add(8)
        }

        val matchTypeCondition = if (parameters.matchType.lowercase() == "all") {
            MATCHES.MATCHTYPE.notIn("t", "wt", "o", "wo", "itt", "witt")
        } else {
            MATCHES.MATCHTYPE.eq(parameters.matchType)
        }

        val result = context.select(
            MATCHES.CAID,
            MATCHES.HOMETEAMNAME,
            MATCHES.AWAYTEAMNAME,
            MATCHES.LOCATION.`as`("ground"),
            MATCHES.LOCATIONID,
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHTITLE,
            MATCHES.TOURNAMENT,
            MATCHES.RESULTSTRING
        ).from(MATCHES)
            .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
            .where(
                (
                        MATCHES.HOMETEAMNAME.like(likeHomeTeamName)
                            .and(MATCHES.AWAYTEAMNAME.like(likeAwayTeamName))
                            .and(EXTRAMATCHDETAILS.TEAMID.eq(MATCHES.HOMETEAMID)))
                    .or(
                        MATCHES.AWAYTEAMNAME.like(likeHomeTeamName)
                            .and(MATCHES.HOMETEAMNAME.like(likeAwayTeamName))
                            .and(EXTRAMATCHDETAILS.TEAMID.eq(MATCHES.AWAYTEAMID))
                            .and(EXTRAMATCHDETAILS.RESULT.`in`(parameters.matchResult))
                    )
                    .and(EXTRAMATCHDETAILS.RESULT.`in`(matchresult))
                    .and(MATCHES.VICTORYTYPE.`in`(victoryType))
                    .and(EXTRAMATCHDETAILS.HOMEAWAY.`in`(venue))
                    .and(MATCHES.MATCHSTARTDATEASOFFSET.ge(parameters.startDate.value))
                    .and(MATCHES.MATCHSTARTDATEASOFFSET.le(parameters.endDate.value))
                    .and(matchTypeCondition)
            )
            .orderBy(MATCHES.MATCHSTARTDATEASOFFSET)
            .fetch()

        val matches = mutableListOf<MatchListDto>()
        result.forEach { record ->
            matches.add(
                MatchListDto(
                    caId = record.get("CaId", String::class.java),
                    homeTeamName = record.get("HomeTeamName", String::class.java),
                    awayTeamName = record.get("AwayTeamName", String::class.java),
                    location = record.get("Location", String::class.java),
                    locationId = record.get("LocationId", Int::class.java),
                    matchDate = LocalDate.parse(record.get("MatchStartDate", String::class.java))
                        .format(formatFullMonths),
                    matchTitle = record.get("MatchTitle", String::class.java),
                    tournament = record.get("Tournament", String::class.java),
                    resultString = record.get("ResultString", String::class.java),
                    key = ""
                )
            )
        }

        return matches.right()
    }

    /**
     * Determines a list of ordinal values representing victory types based on the given match result.
     *
     * @param matchResult The match result as an optional integer. Different values yield different
     *                    sets of victory types. Null or undefined values produce a default set of ordinals.
     * @return A list of ordinal integers corresponding to victory types, as defined by the VictoryType enum.
     *         The list varies based on the value of matchResult.
     */
    private fun getVictoryValuesFromMatchResult(matchResult: Int?): List<Int> {
        return when (matchResult) {
            null, 0 -> listOf(
                VictoryType.Awarded.ordinal,
                VictoryType.Innings.ordinal,
                VictoryType.Runs.ordinal,
                VictoryType.RunRate.ordinal,
                VictoryType.Wickets.ordinal,
                VictoryType.LosingFewerWickets.ordinal,
                VictoryType.FasterScoringRate.ordinal,
                VictoryType.Drawn.ordinal,
                VictoryType.Tied.ordinal,
                VictoryType.NoResult.ordinal,
                VictoryType.Abandoned.ordinal,
                VictoryType.Unknown.ordinal
            )

            1, 5 -> listOf(
                VictoryType.Awarded.ordinal,
                VictoryType.Innings.ordinal,
                VictoryType.Runs.ordinal,
                VictoryType.RunRate.ordinal,
                VictoryType.Wickets.ordinal,
                VictoryType.LosingFewerWickets.ordinal,
                VictoryType.FasterScoringRate.ordinal,
                VictoryType.Unknown.ordinal
            )

            2, 6 -> listOf(
                VictoryType.Innings.ordinal,
                VictoryType.Unknown.ordinal
            )

            3, 7 -> listOf(
                VictoryType.Runs.ordinal,
                VictoryType.Unknown.ordinal
            )

            4, 8 -> listOf(
                VictoryType.Wickets.ordinal,
                VictoryType.Unknown.ordinal
            )

            9 -> listOf(
                VictoryType.Drawn.ordinal,
                VictoryType.Unknown.ordinal
            )

            10 -> listOf(
                VictoryType.Tied.ordinal,
                VictoryType.Unknown.ordinal
            )

            11 -> listOf(
                VictoryType.NoResult.ordinal,
                VictoryType.Unknown.ordinal
            )

            else -> listOf()
        }
    }

    /**
     * Returns a list of integers representing the venue. If the provided `venue` list is null or empty,
     * or if the first element of the list is 0, it returns a default list of [1, 2, 4].
     *
     * @param venue A nullable list of integers representing a venue. Can be null or empty.
     * @return A list of integers. Returns the given `venue` if valid; otherwise, returns a default list of [1, 2, 4].
     */
    private fun getVenue(venue: List<Int>?): List<Int> {
        if (venue == null || venue.size == 0) return listOf(1, 2, 4)
        if (venue[0] == 0) return listOf(1, 2, 4)
        return venue
    }


}