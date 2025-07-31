package com.knowledgespike.feature.player.domain.usecase

import arrow.core.Either
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.feature.player.domain.model.*
import com.knowledgespike.feature.player.domain.repository.PlayerRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedPlayerSearchRequestParameters

/**
 * A collection of use cases related to player operations in a cricket context.
 *
 * This data class aggregates multiple use case classes, each responsible for performing
 * specific operations related to player information retrieval and searching. These use cases
 * are provided as dependencies for seamless integration and access to various player-related
 * functionalities.
 *
 * @property getPlayerBiographyUseCase Used to retrieve a player's biographical details.
 * @property getPlayerOverallUseCase Used to fetch a player's overall performance statistics.
 * @property getBattingDetailsUseCase Used to fetch detailed batting statistics for a player.
 * @property getBowlingDetailsUseCase Used to fetch detailed bowling statistics for a player.
 * @property findPlayer Used to search for players based on specific search parameters.
 */
data class PlayerUseCases(
    val getPlayerBiographyUseCase: GetPlayerBiographyUseCase,
    val getPlayerOverallUseCase: GetPlayerOverallUseCase,
    val getBattingDetailsUseCase: GetPlayerBattingDetailsUseCase,
    val getBowlingDetailsUseCase: GetPlayerBowlingDetailsUseCase,
    val findPlayer: FindPlayerUseCase
)


/**
 * Use case for fetching the biography details of a cricket player.
 *
 * This class provides the functionality to retrieve a player's biography using their unique identifier.
 * It leverages the `PlayerRepository` to access the required data and returns the result either
 * as a successful `PlayerBiographyDto` object or as an instance of `Error` in case of failure.
 *
 * @property repository The repository used to manage and retrieve player-related data.
 */
class GetPlayerBiographyUseCase(val repository: PlayerRepository) {
    /**
     * Retrieves the biography details of a player using their unique identifier.
     *
     * This function acts as an operator overload to simplify the process of invoking
     * the use case for fetching player biography data. It delegates the retrieval logic
     * to the underlying repository.
     *
     * @param id The unique identifier of the player whose biography is to be fetched.
     * @return An [Either] instance containing either:
     *         - [Error]: An error object indicating the failure reason if retrieval fails.
     *         - [PlayerBiographyDto]: The player's biography data encapsulated in a data transfer object.
     */
    operator fun invoke(id: PlayerId) : Either<Error, PlayerBiographyDto> {
        return repository.getPlayerBiography(id)
    }
}

/**
 * Use case for retrieving the overall performance statistics of a cricket player.
 *
 * This class provides a functionality to fetch aggregated performance data, such as batting and bowling details,
 * for a specified player based on their unique identifier. The performance details are grouped into lists,
 * representing various match types or teams.
 *
 * @property repository The repository instance used to access player-related data, including their overall performance details.
 */
class GetPlayerOverallUseCase(val repository: PlayerRepository) {
    /**
     * Invokes the use case to retrieve the overall performance details of a player.
     *
     * This operator function allows fetching a player's aggregated performance statistics,
     * such as batting and bowling details, grouped by specific criteria (e.g., match types, teams).
     *
     * @param id The unique identifier of the player whose overall performance details are to be fetched.
     * @return Either an Error object indicating the reason for failure or a list of lists
     * containing the player's performance details represented as PlayerOverallDto objects.
     */
    operator fun invoke(id: PlayerId) : Either<Error, List<List<PlayerOverallDto>>> {
        return repository.getPlayerOverall(id)
    }
}
/**
 * Use case for retrieving the batting details of a specific cricket player.
 *
 * This class provides a method that retrieves the batting statistics of a player,
 * grouped by match type (e.g., Test, ODI, T20), using the associated `PlayerRepository`.
 *
 * @constructor Initializes the use case with the provided `PlayerRepository` implementation.
 * @param repository The repository responsible for accessing and managing player data.
 */
class GetPlayerBattingDetailsUseCase(val repository: PlayerRepository) {
    /**
     * Retrieves the batting details of a player for various matches.
     *
     * This operator function fetches detailed batting statistics for a player, grouped by match type,
     * and returns the data encapsulated within a map where the key represents the match type
     * and the value is a list of `BattingDetailsDto` objects for that match type.
     *
     * @param id The unique identifier of the player whose batting details are to be retrieved.
     * @return Either an `Error` containing the failure reason or a map where the keys are match types
     *         (e.g., Test, ODI, T20) and the values are lists of `BattingDetailsDto` representing the player's
     *         batting details for each match type.
     */
    operator fun invoke(id: PlayerId) : Either<Error, Map<String, List<BattingDetailsDto>>> {
        return repository.getPlayerBattingDetails(id)
    }
}

/**
 * Use case for retrieving a player's bowling details.
 *
 * This use case provides a method to fetch bowling performance statistics for a specific player,
 * categorized by match types such as Test, ODI, or T20. It relies on the `PlayerRepository`
 * to retrieve the data. The result encapsulates bowling metrics such as runs, wickets, maidens,
 * and other relevant details within a structured map.
 *
 * @property repository The repository responsible for accessing player data.
 */
class GetPlayerBowlingDetailsUseCase(val repository: PlayerRepository) {
    /**
     * Retrieves the bowling details of a player identified by the given player ID.
     *
     * @param id The unique identifier of the player whose bowling details are to be fetched.
     * @return An `Either` instance that contains either:
     * - An `Error` object if the retrieval fails.
     * - A `Map` where each key represents a match type (e.g., Test, ODI, T20), and the value is a list
     *   of `BowlingDetailsDto` objects representing the player's bowling performance across matches.
     */
    operator fun invoke(id: PlayerId) : Either<Error, Map<String, List<BowlingDetailsDto>>> {
        return repository.getPlayerBowlingDetails(id)
    }
}

/**
 * Use case for searching and retrieving a list of players based on validated search parameters.
 *
 * This class is responsible for invoking the `findPlayer` method of the `PlayerRepository` to fetch
 * a list of players that match the specified criteria provided in the `ValidatedPlayerSearchRequestParameters`.
 *
 * @property repository The repository instance that provides data retrieval functionality for player-related operations.
 */
class FindPlayerUseCase(val repository: PlayerRepository) {
    /**
     * Invokes the use case to find players based on the provided search parameters.
     *
     * @param parameters The validated player search request parameters, which include criteria
     *                   like player name, team, exact match preference, date range, and sorting options.
     * @return Either an error if the operation fails, or a database result containing a list
     *         of players matching the specified search criteria.
     */
    operator fun invoke(parameters: ValidatedPlayerSearchRequestParameters) : Either<Error, DatabaseResult<PlayerListDto>> {
        return repository.findPlayer(parameters)
    }
}


