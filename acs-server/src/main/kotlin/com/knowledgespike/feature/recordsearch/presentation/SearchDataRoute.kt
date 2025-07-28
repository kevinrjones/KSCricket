package com.knowledgespike.feature.recordsearch.presentation


import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.fold
import arrow.core.raise.zipOrAccumulate
import com.knowledgespike.core.type.dto.Country
import com.knowledgespike.core.type.dto.Ground
import com.knowledgespike.core.type.dto.Team
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.MatchTypeError
import com.knowledgespike.core.type.error.SeasonError
import com.knowledgespike.core.type.error.VenueIdError
import com.knowledgespike.core.type.json.Envelope
import com.knowledgespike.core.type.values.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedMatchSearchParameters
import com.knowledgespike.feature.recordsearch.domain.usecase.RecordSearchUseCases
import com.knowledgespike.plugins.AUTH_JWT_READ
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.net.URLDecoder


private const val API_TEAMS = "/api/teams"
private const val API_DATES = "/api/matches/dates"
private const val API_GROUNDS = "/api/grounds"
private const val API_COUNTRIES = "/api/countries"
private const val API_MATCHES = "/api/matches"
private const val FIND_MATCHES_ROUTE = "/findmatches"

private const val SERIESDATES_ROUTE = "seriesdates"
private const val SERIESDATES_FORMATCHTYPES_ROUTE = "seriesdatesformatchtypes"
private const val TOURNAMENTS_FOR_SEASON_ROUTE = "tournamentsforseason"
private const val MATCHES_IN_TOURNAMENT_ROUTE = "matchesintournament"

private const val MATCH_TYPE = "matchType"

private const val COUNTRY_ID = "countryId"

private const val MATCHTYPES = "matchtypes"

private const val SEASON = "season"

private const val TOURNAMENT = "tournament"

fun Application.routeSearchParameters() {

    val searchUseCase by inject<RecordSearchUseCases>()
    routing {
        authenticate(AUTH_JWT_READ) {
            route(API_TEAMS) {

                get("/{$MATCH_TYPE}") {

                    fold(
                        block = { MatchType(call.parameters[MATCH_TYPE]) },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        transform = { matchType ->
                            val teams = searchUseCase.getTeamsForCompetitionAndCountry(
                                matchType,
                                CountryId.default()
                            ).map { team ->
                                Team(team.id, team.name, matchType.value)
                            }
                            val env = Envelope.success(teams)
                            call.respond(HttpStatusCode.OK, env)
                        }
                    )
                }

                get("/{$MATCH_TYPE}/{$COUNTRY_ID}") {

                    val maybeValidated = validateMatchTypeAndCountryId(
                        call.parameters[MATCH_TYPE],
                        call.parameters[COUNTRY_ID]?.toInt()
                    )

                    maybeValidated.fold(
                        ifLeft = {
                            val env = Envelope.failure(it.joinToString { "," })
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { parameters ->
                            val teams = searchUseCase.getTeamsForCompetitionAndCountry(
                                parameters.first, parameters.second
                            ).map {
                                Team(it.id, it.name, parameters.first.value)
                            }
                            val env = Envelope.success(teams)
                            call.respond(HttpStatusCode.OK, env)
                        })
                }

            }
            route(API_GROUNDS) {

                get("/{$MATCH_TYPE}") {

                    fold<MatchTypeError, MatchType, Unit>(
                        block = { MatchType(call.parameters[MATCH_TYPE]) },
                        transform = { matchType -> getGrounds(searchUseCase, matchType, CountryId.default()) },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        })

                }

                get("/{$MATCH_TYPE}/{$COUNTRY_ID}") {


                    val maybeValidated = validateMatchTypeAndCountryId(
                        call.parameters[MATCH_TYPE],
                        call.parameters[COUNTRY_ID]?.toInt()
                    )


                    maybeValidated.fold(
                        ifLeft = {
                            val env = Envelope.failure(it.joinToString { "," })
                            call.respond(HttpStatusCode.BadRequest, env)
                        },
                        ifRight = { parameters ->

                            getGrounds(searchUseCase, parameters.first, parameters.second)
                        })
                }
            }
            route(API_COUNTRIES) {
                get("/{$MATCH_TYPE}") {

                    fold(
                        block = { MatchType(call.parameters[MATCH_TYPE]) },
                        transform = { matchType ->

                            val countries = searchUseCase.getCountriesForCompetition(
                                matchType
                            ).map {
                                Country(it.id, it.name, matchType.value)
                            }
                            val env = Envelope.success(countries)
                            call.respond(HttpStatusCode.OK, env)
                        },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        }
                    )
                }
            }

            route(API_MATCHES) {


                get("/$SERIESDATES_ROUTE/{$MATCH_TYPE}") {
                    fold(
                        block = { MatchType(call.parameters[MATCH_TYPE]) },
                        transform = { matchType ->
                            val dates = searchUseCase.getSeriesDatesCompetition(matchType)

                            val env = Envelope.success(dates)
                            call.respond(HttpStatusCode.OK, env)
                        },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        }
                    )
                }

                get("/$SERIESDATES_FORMATCHTYPES_ROUTE/{$MATCHTYPES...}") {

                    fold(
                        block = {
                            val matchTypes = call.parameters.getAll(MATCHTYPES)
                            if (matchTypes == null || matchTypes.isEmpty())
                                raise(MatchTypeError("No match types provided", ""))

                            matchTypes.map { MatchType(it) }

                        },
                        transform = { matchTypes ->
                            val maybeDates: Either<Error, HashMap<Int, List<String>>> =
                                searchUseCase.getSeriesDatesForMatchTypes(matchTypes)

                            maybeDates.fold(
                                ifLeft = {
                                    val env = Envelope.failure(it.message)
                                    call.respond(HttpStatusCode.BadRequest, env)
                                },
                                ifRight = {
                                    val env = Envelope.success(it)
                                    call.respond(HttpStatusCode.OK, env)
                                }
                            )

                        },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        }
                    )
                }

                get("/$TOURNAMENTS_FOR_SEASON_ROUTE/{$SEASON}") {

                    fold(
                        block = {
                            val maybeSeason = call.parameters[SEASON]

                            val season = if (maybeSeason == null)
                                raise(SeasonError("Invalid $SEASON"))
                            else
                                URLDecoder.decode(maybeSeason, "UTF-8")

                            val matchTypes = call.parameters.getAll(MATCHTYPES)
                            if (matchTypes == null || matchTypes.isEmpty())
                                raise(MatchTypeError("No match types provided", ""))

                            Pair(season, matchTypes.map { MatchType(it) })

                        },
                        transform = { (season, matchTypes) ->
                            val maybeDates =
                                searchUseCase.getTournamentsForSeason(season, matchTypes)

                            maybeDates.fold(
                                ifLeft = {
                                    val env = Envelope.failure(it.message)
                                    call.respond(HttpStatusCode.BadRequest, env)
                                },
                                ifRight = {
                                    val env = Envelope.success(it)
                                    call.respond(HttpStatusCode.OK, env)
                                }
                            )

                        },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        }
                    )
                }

                get("/$MATCHES_IN_TOURNAMENT_ROUTE/{$TOURNAMENT}") {

                    fold(
                        block = {
                            val maybeTournament = call.parameters[TOURNAMENT]

                            val tournament = if (maybeTournament == null)
                                raise(SeasonError("Invalid $SEASON"))
                            else
                                URLDecoder.decode(maybeTournament, "UTF-8")

                            tournament

                        },
                        transform = { tournament ->
                            val maybeMatches =
                                searchUseCase.getMatchesForTournaments(tournament)

                            maybeMatches.fold(
                                ifLeft = {
                                    val env = Envelope.failure(it.message)
                                    call.respond(HttpStatusCode.BadRequest, env)
                                },
                                ifRight = {
                                    val env = Envelope.success(it)
                                    call.respond(HttpStatusCode.OK, env)
                                }
                            )

                        },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        }
                    )
                }

                route(FIND_MATCHES_ROUTE) {
                    get() {
                        fold(
                            block = {
                                val requestParameters = extractRequestParameters(call)
                                validateParameters(requestParameters).fold(
                                    ifLeft = {
                                        raise(it)
                                    },
                                    ifRight = {
                                        it
                                    }
                                )
                            },
                            transform = { requestParameters ->
                                val result = searchUseCase.findMatches(requestParameters)

                                result.fold(
                                    ifLeft = {
                                        val env = Envelope.failure(it.message)
                                        call.respond(HttpStatusCode.BadRequest, env)
                                    },
                                    ifRight = {
                                        val env = Envelope.success(it)
                                        call.respond(HttpStatusCode.OK, env)

                                    }
                                )
                            },
                            recover = {
                                val env = Envelope.failure(it.map { error -> error.message }.joinToString(","))
                                call.respond(HttpStatusCode.BadRequest, env)
                            })
                    }
                }
            }

            route(API_DATES) {
                get("/{$MATCH_TYPE}") {
                    fold(
                        block = { MatchType(call.parameters[MATCH_TYPE]) },
                        transform = { matchType ->
                            val dates = searchUseCase.getStartAndEndDatesCompetition(matchType)

                            val env = Envelope.success(dates)
                            call.respond(HttpStatusCode.OK, env)
                        },
                        recover = {
                            val env = Envelope.failure(it.message)
                            call.respond(HttpStatusCode.BadRequest, env)
                        })
                }

            }
        }
    }
}

private fun extractRequestParameters(call: RoutingCall): RequestParameters {
    return RequestParameters(
        homeTeam = call.request.queryParameters["homeTeam"] ?: "",
        awayTeam = call.request.queryParameters["awayTeam"] ?: "",
        homeTeamExactMatch = call.request.queryParameters["homeTeamExactMatch"]?.toBoolean(),
        awayTeamExactMatch = call.request.queryParameters["awayTeamExactMatch"]?.toBoolean(),
        startDate = call.request.queryParameters["startDate"] ?: "0001-01-01",
        endDate = call.request.queryParameters["endDate"] ?: "9999-12-31",
        venue = call.request.queryParameters.getAll("venue"),
        matchResult = call.request.queryParameters["matchResult"]?.toInt(),
        matchType = call.request.queryParameters["matchType"],
    )

}

private fun validateParameters(requestParameters: RequestParameters): Either<NonEmptyList<Error>, ValidatedMatchSearchParameters> =
    either {
        val format = "yyyy-MM-dd"
        val earliestDate = "1772-06-01"
        val latestDate = "9999-12-31"
        zipOrAccumulate(
            {
                MatchTypeOrAll(requestParameters.matchType)
            },
            {
                ThreeCharacterSearchTerm(requestParameters.homeTeam)
            },
            {
                ThreeCharacterSearchTerm(requestParameters.awayTeam)
            },
            {
                if (requestParameters.startDate == null || requestParameters.startDate.isEmpty())
                    EpochDate(earliestDate, format)
                else
                    EpochDate(requestParameters.startDate, format)
            },
            {
                if (requestParameters.endDate == null || requestParameters.endDate.isEmpty())
                    EpochDate(latestDate, format)
                else
                    EpochDate(requestParameters.endDate, format)
            },
            {
                requestParameters.venue?.map { venue -> venue.toIntOrNull() ?: raise(VenueIdError("Venue id ($venue) is invalid", venue.toInt())) }
            }
        ) { matchType,  homeTeam, awayTeam, startDate, endDate, venue ->
            ValidatedMatchSearchParameters(
                homeTeam = homeTeam.value,
                awayTeam = awayTeam.value,
                homeTeamExactMatch = requestParameters.homeTeamExactMatch ?: true,
                awayTeamExactMatch = requestParameters.awayTeamExactMatch ?: true,
                startDate = startDate,
                endDate = endDate,
                venue = venue,
                matchResult = requestParameters.matchResult,
                matchType = matchType.value,
            )
        }
    }

private data class RequestParameters(
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamExactMatch: Boolean?,
    val awayTeamExactMatch: Boolean?,
    val startDate: String? = null,
    val endDate: String? = null,
    val venue: List<String>? = null,
    val matchResult: Int? = null,
    val matchType: String? = null,
)

suspend fun RoutingContext.getGrounds(
    searchUseCase: RecordSearchUseCases,
    matchType: MatchType,
    countryId: CountryId
) {
    val grounds = searchUseCase.getGroundsForCompetitionAndCountry(
        matchType, countryId
    ).map {
        Ground(it.id, it.matchType, it.code, it.countryName, it.groundId, it.knownAs)
    }
    val env = Envelope.success(grounds)
    call.respond(HttpStatusCode.OK, env)
}


fun validateMatchTypeAndCountryId(
    matchType: String?,
    countryId: Int?
): Either<NonEmptyList<Error>, Pair<MatchType, CountryId>> =


    either {
        zipOrAccumulate(
            { MatchType(matchType) },
            { CountryId(countryId) }
        ) { matchType, countryId ->
            Pair(matchType, countryId)
        }
    }


