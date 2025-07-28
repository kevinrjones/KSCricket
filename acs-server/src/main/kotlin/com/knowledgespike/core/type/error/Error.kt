package com.knowledgespike.core.type.error

import kotlinx.serialization.Serializable


@Serializable
sealed class Error(val message: String)

class MatchTypeError(message: String, val matchType: String) : Error(message)
class TeamIdError(message: String, val teamId: Int?) : Error(message)
class PlayerIdError(message: String, val playerId: Int?) : Error(message)
class GroundIdError(message: String, val groundId: Int?) : Error(message)
class CaIdError(message: String, val caId: String?) : Error(message)
class CountryIdError(message: String, val countryId: Int?) : Error(message)
class VenueIdError(message: String, val venueId: Int?) : Error(message)
class SortDirectionError(message: String, val sortDirection: String) : Error(message)
class SeasonError(message: String) : Error(message)
class FindError(message: String) : Error(message)
class YearsError(message: String) : Error(message)
class UrlError(message: String) : Error(message)
class SearchTeamNameError(message: String) : Error(message)
class NullError(message: String) : Error(message)
class DateTimeError(message: String) : Error(message)
class IntegerError(message: String) : Error(message)
