package com.knowledgespike.feature.recordsearch.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.YearsError
import com.knowledgespike.core.type.values.CountryId
import com.knowledgespike.core.type.values.MatchType
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jooq.DSLContext
import org.jooq.Record4
import org.jooq.SelectSeekStep1
import org.jooq.SortField
import org.jooq.impl.DSL.and
import org.jooq.impl.DSL.select

/**
 * The `JooqSearch` object provides a collection of methods to interact with a SQL database
 * using the Jooq DSLContext. It enables fetching and processing match types, competitions, grounds,
 * countries, series dates, tournaments, and matches for different queries defined by match types,
 * seasons, or other provided filters.
 */
object JooqSearch {

    /**
     * Retrieves a list of match types from the database.
     *
     * @param context The DSLContext used to execute the database query.
     * @return A list of MatchTypeEntity objects containing match type information.
     */
    fun getMatchTypes(context: DSLContext): List<MatchTypeEntity> {

        val matchTypes = mutableListOf<MatchTypeEntity>()

        val result = context.select(
            MATCHTYPES.ID,
            MATCHTYPES.MATCHTYPE,
            MATCHTYPES.DESCRIPTION
        ).from(MATCHTYPES).fetch()


        result.forEach { matchSubType ->
            val id = matchSubType.getValue("Id", Int::class.java)
            val type = matchSubType.getValue("MatchType", String::class.java)
            val description = matchSubType.getValue("Description", String::class.java)

            matchTypes.add(MatchTypeEntity(id, type, description))
        }

        return matchTypes
    }


    /**
     * Fetches a list of competitions from the database based on the specified match type.
     *
     * @param context The DSLContext used for interacting with the database.
     * @param matchType The type of match used to filter competitions.
     * @return A list of competitions matching the specified match type.
     */
    fun getCompetitions(context: DSLContext, matchType: MatchType): List<Competition> {

        val result = context.select(
            COMPETITIONS.ID,
            COMPETITIONS.MATCHTYPE,
            COMPETITIONS.MATCHSUBTYPE,
            COMPETITIONS.COMPETITION
        ).from(COMPETITIONS)
            .where(COMPETITIONS.MATCHTYPE.eq(matchType.value))
            .fetch()

        val competitions = mutableListOf<Competition>()
        result.forEach { matchSubType ->
            val id = matchSubType.getValue("Id", Int::class.java)
            val type = matchSubType.getValue("MatchType", String::class.java)
            val subtype = matchSubType.getValue("MatchSubType", String::class.java)
            val description = matchSubType.getValue("Competition", String::class.java)

            competitions.add(Competition(id, type, subtype, description))
        }

        return competitions

    }


    /**
     * Retrieves a list of grounds based on the specified match type and country.
     *
     * @param context The DSLContext used for database interactions.
     * @param matchType The type of match to filter the grounds.
     * @param countryId The identifier of the country to filter the grounds. If the ID is 0, all countries are included.
     * @return A list of grounds that match the specified criteria, with an additional entry for "All Grounds" at the start.
     */
    fun getGroundsForCompetitionAndCountry(
        context: DSLContext,
        matchType: MatchType,
        countryId: CountryId,
    ): List<Ground> {


        val countryClause = if (countryId.id == 0) {
            and(GROUNDSMATCHTYPES.grounds.COUNTRYID.ne(0))
        } else {
            and(
                GROUNDSMATCHTYPES.grounds.COUNTRYID
                    .eq(countryId.id)
            )
        }

        val result = context.select(
            GROUNDS.ID,
            COUNTRYCODES.CODE,
            COUNTRYCODES.COUNTRY.`as`("CountryName"),
            GROUNDSMATCHTYPES.GROUNDID,
            GROUNDSMATCHTYPES.grounds.KNOWNAS,
        ).from(GROUNDSMATCHTYPES)
            .join(GROUNDS).on(GROUNDS.ID.eq(GROUNDSMATCHTYPES.GROUNDID))
            .join(COUNTRYCODES).on(GROUNDS.COUNTRYNAME.eq(COUNTRYCODES.COUNTRY))
            .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchType.value))
            .and(countryClause)
            .orderBy(GROUNDSMATCHTYPES.grounds.KNOWNAS)
            .fetch()

        val grounds = mutableListOf<Ground>()
        result.forEach { matchSubType ->
            val id = matchSubType.getValue("Id", Int::class.java)
            val groundId = matchSubType.getValue("GroundId", Int::class.java)
            val knownAs = matchSubType.getValue("KnownAs", String::class.java)
            val countryName = matchSubType.getValue("CountryName", String::class.java)
            val code = matchSubType.getValue("Code", String::class.java)

            grounds.add(Ground(id, matchType.value, code, countryName, groundId, knownAs))
        }

        grounds.add(0, Ground(0, "", "", "", 0, "All Grounds"))
        return grounds

    }


    /**
     * Retrieves a list of distinct countries associated with a specified match type.
     * This includes all countries where the match type is played, ordered by country name.
     * Additionally, a default country representing "All Countries" is added to the list.
     *
     * @param context The DSLContext used to interact with the database.
     * @param matchType The match type for which countries need to be retrieved.
     * @return A list of countries associated with the given match type, including a default entry for "All Countries".
     */
    fun getCountriesForCompetition(
        context: DSLContext,
        matchType: MatchType
    ): List<Country> {
        val result = context.selectDistinct(
            GROUNDS.COUNTRYID,
            GROUNDS.COUNTRYNAME,
        ).from(GROUNDS)
            .join(GROUNDSMATCHTYPES).on(GROUNDSMATCHTYPES.GROUNDID.eq(GROUNDS.ID))
            .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchType.value))
            .orderBy(GROUNDS.COUNTRYNAME)
            .fetch()

        val countries = mutableListOf<Country>()
        result.forEach { matchSubType ->
            val id = matchSubType.getValue("CountryId", Int::class.java)
            val name = matchSubType.getValue("CountryName", String::class.java)

            countries.add(Country(id, name))
        }

        countries.add(0, Country(0, "All Countries"))

        return countries

    }

    /**
     * Retrieves a distinct list of series dates for matches of a specified match type from the database.
     * The result includes "All Seasons" as the first entry, followed by the series dates.
     *
     * @param context The DSLContext used to build and execute the SQL query.
     * @param matchType The type of match used to filter the matches whose series dates should be retrieved.
     * @return A list of series dates as strings, including "All Seasons" as the first entry.
     */
    fun getSeriesDateForCompetition(
        context: DSLContext, matchType: MatchType
    ): List<String> {
        val result = context.selectDistinct(
            MATCHES.SERIESDATE
        ).from(MATCHES)
            .where(
                MATCHES.ID.`in`(
                    select(MATCHSUBTYPE.MATCHID).from(
                        MATCHSUBTYPE.where(
                            MATCHSUBTYPE.MATCHTYPE.eq(
                                matchType.value
                            )
                        )
                    )
                )
            )
            .fetch()

        val seriesDates = mutableListOf<String>()
        result.forEach { record ->
            val date = record.getValue("SeriesDate", String::class.java)

            seriesDates.add(date)
        }

        seriesDates.add(0, "All Seasons")

        return seriesDates

    }

    /**
     * Retrieves a mapping of series dates grouped by decades for specified match types.
     *
     * The method fetches distinct series dates from the database where the match type matches
     * any of the provided match types. The retrieval is performed in ascending order of the
     * series date. The series dates are then grouped by their respective decades.
     *
     * @param context The database context used to perform the query.
     * @param matchTypes A list of MatchType objects representing the types of matches to filter the series dates by.
     * @return An Either containing:
     *         - A HashMap where the keys are decades (as integers) and the values are lists of series dates as strings,
     *           on successful retrieval and conversion.
     *         - An Error if a failure occurs during data processing.
     */
    fun getSeriesDateForMatchTypes(
        context: DSLContext,
        matchTypes: List<MatchType>
    ): Either<Error, HashMap<Int, List<String>>> {
        val result = context.selectDistinct(
            MATCHES.SERIESDATE
        ).from(MATCHES)
            .where(
                MATCHES.MATCHTYPE.`in`(
                    matchTypes.map { it.value }
                )
            ).orderBy(MATCHES.SERIESDATE)
            .fetch()

        val seriesDates = mutableListOf<String>()
        result.forEach { record ->
            val date = record.getValue("SeriesDate", String::class.java)

            seriesDates.add(date)
        }

        return convertToByDecade(seriesDates)

    }

    /**
     * Retrieves the list of tournaments for a given season and matching the specified match types.
     *
     * @param context The `DSLContext` used to interact with the database.
     * @param season The season (as a string) for which tournaments are to be fetched.
     * @param matchTypes A list of `MatchType` representing the types of matches to filter the tournaments.
     * @return An `Either` containing an `Error` if the operation fails or a `List` of tournament names if successful.
     */
    fun getTournamentsForSeason(
        context: DSLContext,
        season: String, matchTypes: List<MatchType>
    ): Either<Error, List<String>> {
        val result = context.selectDistinct(
            MATCHES.TOURNAMENT
        ).from(MATCHES)
            .where(
                MATCHES.MATCHTYPE.`in`(
                    matchTypes.map { it.value }
                ).and(MATCHES.SERIESDATE.eq(season))
            ).orderBy(MATCHES.TOURNAMENT)
            .fetch()

        val seriesDates = mutableListOf<String>()
        result.forEach { record ->
            val date = record.getValue("Tournament", String::class.java)

            seriesDates.add(date)
        }
        return seriesDates.right()
    }

    /**
     * Retrieves the list of matches for a specific tournament from the database.
     *
     * @param context The DSLContext used for database interaction.
     * @param tournament The name of the tournament for which matches are being retrieved.
     * @return Either an instance of Error in case of a failure, or a List of MatchListDto
     * containing the details of the matches for the specified tournament.
     */
    @OptIn(FormatStringsInDatetimeFormats::class)
    fun getMatchesForTournament(
        context: DSLContext,
        tournament: String
    ): Either<Error, List<MatchListDto>> {
        val result = context.selectDistinct(
            MATCHES.HOMETEAMNAME,
            MATCHES.AWAYTEAMNAME,
            MATCHES.LOCATION.`as`("ground"),
            MATCHES.LOCATIONID,
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHTITLE,
            MATCHES.TOURNAMENT,
            MATCHES.RESULTSTRING,
            MATCHES.CAID
        ).from(MATCHES)
            .where(
                MATCHES.TOURNAMENT.eq(tournament)
            ).orderBy(MATCHES.MATCHSTARTDATEASOFFSET)
            .fetch()

        val matches = mutableListOf<LocalMatchListDto>()
        result.forEach { record ->
            matches.add(
                LocalMatchListDto(
                    caId = record.getValue("CaId", String::class.java),
                    homeTeamName = record.getValue("HomeTeamName", String::class.java),
                    awayTeamName = record.getValue("AwayTeamName", String::class.java),
                    location = record.getValue("Location", String::class.java),
                    locationId = record.getValue("LocationId", Int::class.java),
                    matchStartDate = LocalDate.parse(record.getValue("MatchStartDate", String::class.java)),
                    matchTitle = record.getValue("MatchTitle", String::class.java),
                    tournament = record.getValue("Tournament", String::class.java),
                    resultString = record.getValue("ResultString", String::class.java),
                )
            )
        }

        val formattedMatches = matches.map { ml ->
            val date = ml.matchStartDate.format(formatFullMonths)
            val key = ml.matchStartDate.format(LocalDate.Format { byUnicodePattern("yyyy/MM/dd") })
            // val date = DateTime.ParseExact(Regex.Replace(ml.MatchStartDate, "(%dth|%dst|%dnd|%drd)", "")
            //     , "d MMMM yyyy"
            //     , CultureInfo.CurrentCulture).ToString("dd MMM yyyy");

            MatchListDto(
                ml.caId, ml.homeTeamName, ml.awayTeamName, ml.location, ml.locationId, date,
                ml.matchTitle, ml.tournament, ml.resultString,
                "${ml.homeTeamName}-v-${ml.awayTeamName}-$key"
            )
        }.groupBy { it.key }
            .map { it.value.first() }
            .toList()

        return formattedMatches.right()
    }

    /**
     * Converts a series of year strings into a mapping of decades to their corresponding years.
     *
     * @param series A list of year strings to be grouped by decades.
     * @return Either an Error object if the conversion fails or a HashMap where keys are decades (as integers)
     *         and values are lists of year strings belonging to those decades.
     */
    private fun convertToByDecade(series: List<String>): Either<Error, HashMap<Int, List<String>>> {
        val dict = HashMap<Int, MutableList<String>>()

        for (year in series) {
            val yearStart = year.take(3).toIntOrNull()
            if (yearStart == null) {
                return YearsError("Unable to parse years").left()
            }

            val decade = yearStart * 10
            val entry = dict.getOrPut(decade) { mutableListOf() }
            entry.add(year)
        }

        @Suppress("UNCHECKED_CAST")
        return (dict as (HashMap<Int, List<String>>)).right()
    }


    /**
     * Determines the start and end dates for a given competition based on match data.
     *
     * This method queries the database to fetch the earliest and latest match dates
     * of a specified match type. It uses the provided database context and match type
     * information to perform the query and then processes the results to return the
     * start and end dates of the competition.
     *
     * @param context The database context used for executing queries.
     * @param matchType The type of match for which the start and end dates are to be calculated.
     * @return A list containing two `MatchDate` objects: the start date as the first element,
     * and the end date as the second element.
     */
    fun getStartAndEndDatesForCompetition(context: DSLContext, matchType: MatchType): List<MatchDate> {
        var startDate = MatchDate.minimum()
        var endDate = MatchDate.maximum()


        var orderBy = MATCHES.MATCHSTARTDATEASOFFSET.desc()

        var query = getInitialQuery(context, orderBy, matchType)

        val endDateQuery = query
            .limit(1)

        var result = endDateQuery.fetch()

        result.forEach { record ->
            endDate = endDate.copy(
                dateOffset = record.getValue(MATCHES.MATCHSTARTDATEASOFFSET, Long::class.java),
                date = record.getValue(MATCHES.MATCHSTARTDATE, String::class.java),
                matchType = record.getValue(MATCHES.MATCHTYPE, String::class.java),
            )
        }

        orderBy = MATCHES.MATCHSTARTDATEASOFFSET.asc()
        query = getInitialQuery(context, orderBy, matchType)

        val startDateQuery = query
            .limit(1)

        result = startDateQuery.fetch()


        result.forEach { record ->
            startDate = startDate.copy(
                dateOffset = record.getValue(MATCHES.MATCHSTARTDATEASOFFSET, Long::class.java),
                date = record.getValue(MATCHES.MATCHSTARTDATE, String::class.java),
                matchType = record.getValue(MATCHES.MATCHTYPE, String::class.java),
            )

        }

        return listOf(startDate, endDate)
    }


    /**
     * Retrieves a list of teams associated with a specific competition type and country.
     * The resulting list will include a placeholder team "All Teams" at the beginning.
     *
     * @param context The DSLContext instance used for executing the database query.
     * @param matchSubType The specific match type that the teams are associated with.
     * @param countryId The country identifier for filtering teams by home country; use 0 for all countries.
     * @return A list of teams, including a placeholder "All Teams" at the beginning.
     */
    fun getTeamsForCompetitionAndCountry(
        context: DSLContext,
        matchSubType: MatchType,
        countryId: CountryId,
    ): List<Team> {

        val countryClause = if (countryId.id == 0) {
            and(MATCHES.HOMECOUNTRYID.ne(0))
        } else {
            and(
                MATCHES.HOMECOUNTRYID.eq(countryId.id)
            )
        }

        val result = context.selectDistinct(TEAMS.ID, TEAMS.NAME)
            .from(MATCHES)
            .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
            .join(TEAMS).on(TEAMS.ID.eq(EXTRAMATCHDETAILS.TEAMID))
            .where(
                MATCHES.ID.`in`(
                    select(MATCHSUBTYPE.MATCHID).from(
                        MATCHSUBTYPE.where(
                            MATCHSUBTYPE.MATCHTYPE.eq(
                                matchSubType.value
                            )
                        )
                    )
                )
            )
            .and(countryClause)
            .and(TEAMS.ID.ne(1))
            .orderBy(TEAMS.NAME)
            .fetch()

        val teams = mutableListOf<Team>()
        result.forEach { team ->
            val id = team.getValue("Id", Int::class.java)
            val name = team.getValue("Name", String::class.java)

            teams.add(Team(id, name))
        }

        teams.add(0, Team(0, "All Teams"))
        return teams
    }


    /**
     * Constructs an initial query to fetch specific match-related data based on the provided parameters.
     * The query filters matches by certain conditions and orders the result set based on the specified criteria.
     *
     * @param context The DSLContext object used for constructing and executing SQL queries.
     * @param orderBy The sorting field to order the query results by, typically applied on the match identifier.
     * @param matchSubType A `MatchType` object representing the specific type of matches to include in the query.
     * @return A `SelectSeekStep1` query object containing four fields: LocalDate (match start date),
     *         Long (match start date as offset), String (match type), and String (series date),
     *         ordered by the specified field.
     */
    private fun getInitialQuery(
        context: DSLContext,
        orderBy: SortField<Long?>,
        matchSubType: MatchType,
    ): SelectSeekStep1<Record4<java.time.LocalDate?, Long?, String?, String?>, Long?> {
        val endInitialQuery = context.select(
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHSTARTDATEASOFFSET,
            MATCHES.MATCHTYPE,
            MATCHES.SERIESDATE
        ).from(MATCHES)
            .where(
                MATCHES.ID.`in`(
                    select(MATCHSUBTYPE.MATCHID).from(
                        MATCHSUBTYPE.where(
                            MATCHSUBTYPE.MATCHTYPE.eq(
                                matchSubType.value
                            )
                        )
                    )
                )
            ).and(MATCHES.MATCHSTARTDATEASOFFSET.isNotNull).orderBy(orderBy)
        return endInitialQuery
    }
}