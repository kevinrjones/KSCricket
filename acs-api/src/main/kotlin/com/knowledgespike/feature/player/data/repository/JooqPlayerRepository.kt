package com.knowledgespike.feature.player.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.knowledgespike.DIALECT
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.feature.player.data.repository.JooqBattingDetails.getBattingDetails
import com.knowledgespike.feature.player.data.repository.JooqBowlingDetails.getBowlingDetails
import com.knowledgespike.feature.player.data.repository.JooqPlayerBiography.getBiography
import com.knowledgespike.feature.player.data.repository.JooqPlayerOverall.getOverall
import com.knowledgespike.feature.player.domain.model.*
import com.knowledgespike.feature.player.domain.repository.PlayerRepository
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedPlayerSearchRequestParameters
import com.knowledgespike.plugins.DataSource
import org.jooq.impl.DSL.using

/**
 * A repository implementation for accessing and retrieving player-related data using jOOQ.
 * This class interacts with the database through a jOOQ DSL context provided by `DataSource`.
 * Implements the PlayerRepository interface to provide methods for fetching player information.
 *
 * The repository supports operations such as retrieving a player's biography, overall details,
 * batting details, bowling details, and player search functionality based on given parameters.
 *
 * @param dataSource The data source used to manage database connections.
 */
class JooqPlayerRepository(private val dataSource: DataSource) : PlayerRepository {
    /**
     * Retrieves the biography details of a player based on the given player ID.
     *
     * @param id The unique identifier for the player whose biography is to be retrieved.
     * @return Either an error or the player's biography details wrapped in a PlayerBiographyDto.
     */
    override fun getPlayerBiography(id: PlayerId): Either<Error, PlayerBiographyDto> {
        val context = using(dataSource.dataSource, DIALECT)

        val result = getBiography(context, id)

        return result
    }

    /**
     * Retrieves the overall performance data of a player, including both batting and bowling statistics,
     * grouped by match type and team, and returns it as a nested list structure.
     *
     * @param id The unique identifier of the player whose overall performance data is to be retrieved.
     * @return Either an Error if an issue occurs (e.g., player data not found), or a nested list of PlayerOverallDto
     *         objects representing the player's performance data.
     */
    override fun getPlayerOverall(id: PlayerId): Either<Error, List<List<PlayerOverallDto>>> {
        val context = using(dataSource.dataSource, DIALECT)

        val result = getOverall(context, id)

        return result
    }

    /**
     * Retrieves the batting details for a specific player, categorized by match type.
     *
     * @param id The unique identifier of the player whose batting details need to be fetched.
     * @return Either an [Error] in case of a failure or a map where the key is the match type
     * and the value is a list of [BattingDetailsDto] representing the player's batting details.
     */
    override fun getPlayerBattingDetails(id: PlayerId): Either<Error, Map<String, List<BattingDetailsDto>>> {
        val context = using(dataSource.dataSource, DIALECT)

        val result = getBattingDetails(context, id)

        return result
    }

    /**
     * Retrieves bowling details for a player with the specified ID.
     *
     * @param id The ID of the player for whom the bowling details are to be retrieved.
     * @return Either an error object if the operation fails, or a map where the keys represent match types
     *         and the values are lists of BowlingDetailsDto objects containing the player's bowling statistics.
     */
    override fun getPlayerBowlingDetails(id: PlayerId): Either<Error, Map<String, List<BowlingDetailsDto>>> {
        val context = using(dataSource.dataSource, DIALECT)

        val result = getBowlingDetails(context, id)

        return result
    }

    /**
     * Finds players based on the given search parameters.
     *
     * @param parameters The validated search parameters used to filter and sort the list of players.
     * @return Either an error if the operation fails, or a database result containing a list of players that match the criteria.
     */
    override fun findPlayer(parameters: ValidatedPlayerSearchRequestParameters): Either<Error, DatabaseResult<PlayerListDto>> {
        val context = using(dataSource.dataSource, DIALECT)
        return JooqFindPlayer.findPlayer(context, parameters).fold<Either<Error, DatabaseResult<PlayerListDto>>>(
            ifLeft = { return it.left() },
            ifRight = { return DatabaseResult(it, it.size).right() }
        )
    }

}