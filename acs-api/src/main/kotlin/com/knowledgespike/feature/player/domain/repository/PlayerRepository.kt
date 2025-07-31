package com.knowledgespike.feature.player.domain.repository

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.feature.player.domain.model.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedPlayerSearchRequestParameters

/**
 * Repository interface for managing and retrieving cricket player-related data.
 *
 * This interface provides a set of methods to fetch comprehensive details about cricket players,
 * including their biographical information, overall performance, and detailed batting or bowling statistics.
 * It also supports searching for players based on validated search criteria.
 */
interface PlayerRepository {
    /**
     * Retrieves the biography details of a player specified by their unique identifier.
     *
     * @param id The unique identifier of the player whose biography is to be retrieved.
     * @return An [Either] containing either an [Error] if the retrieval fails
     *         or a [PlayerBiographyDto] object representing the player's biography details.
     */
    fun getPlayerBiography(id: PlayerId): Either<Error, PlayerBiographyDto>
    /**
     * Retrieves the overall performance details of a player.
     *
     * This function fetches a player's aggregated performance statistics, such as batting and bowling details,
     * grouped by lists. Each inner list corresponds to specific grouping criteria (e.g., match types, teams).
     *
     * @param id The unique identifier of the player whose overall performance details are to be fetched.
     * @return Either an error object indicating failure or a list of lists containing the player's performance details as PlayerOverallDto objects.
     */
    fun getPlayerOverall(id: PlayerId): Either<Error, List<List<PlayerOverallDto>>>
    /**
     * Retrieves the batting details of a player for various matches.
     *
     * This function fetches detailed batting statistics for a player, grouped by match type,
     * and returns the data encapsulated within a map where the key represents the match type
     * and the value is a list of `BattingDetailsDto` objects for that match type.
     *
     * @param id The unique identifier of the player whose batting details are to be retrieved.
     * @return Either an `Error` containing a failure reason or a map where keys are match types
     *         (e.g., Test, ODI, T20) and values are lists of `BattingDetailsDto` objects with the player's
     *         batting details.
     */
    fun getPlayerBattingDetails(id: PlayerId): Either<Error, Map<String, List<BattingDetailsDto>>>
    /**
     * Fetches the bowling details for a specified player.
     *
     * This method retrieves a mapping of match types to a list of bowling statistics for the player
     * identified by the given `PlayerId`. The response encapsulates performance data such as runs,
     * wickets, maidens, and other bowling-specific metrics across various matches.
     *
     * @param id The unique identifier of the player whose bowling details are to be retrieved.
     * @return An `Either` instance containing either an `Error` object in case of failure
     *         or a `Map` where each key is a match type (e.g., Test, ODI, T20) and the value is
     *         a list of `BowlingDetailsDto` objects representing the player's bowling performance.
     */
    fun getPlayerBowlingDetails(id: PlayerId): Either<Error, Map<String, List<BowlingDetailsDto>>>
    /**
     * Searches for players based on the provided search parameters.
     *
     * @param parameters The validated player search parameters containing criteria
     *                    like name, team, date range, and sorting details.
     * @return Either an error or a database result containing a list of players
     *                     matching the search criteria.
     */
    fun findPlayer(parameters: ValidatedPlayerSearchRequestParameters) : Either<Error, DatabaseResult<PlayerListDto>>
}