package com.knowledgespike.feature.player.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.PlayerIdError
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.db.tables.references.PLAYERS
import com.knowledgespike.feature.player.domain.model.PlayerBiography
import com.knowledgespike.feature.player.domain.model.PlayerBiographyDto
import org.jooq.DSLContext

/**
 * Utility object for retrieving player biography data from the database using jOOQ.
 * Provides functionality for fetching detailed biographical information for a specific player.
 */
object JooqPlayerBiography {

    /**
     * Retrieves the biography of a player by their ID.
     *
     * @param context The DSLContext to execute the database query.
     * @param id The unique identifier of the player to retrieve the biography for.
     * @return Either an Error if the player cannot be found or a PlayerBiographyDto containing the player's biography details.
     */
    fun getBiography(context: DSLContext, id: PlayerId): Either<Error, PlayerBiographyDto> {

        val records = context.select(
            PLAYERS.BIRTHDATEASTEXT,
            PLAYERS.DATEDIEDASTEXT,
            PLAYERS.BATTINGHAND,
            PLAYERS.SHORTBOWLINGSTYLES,
            PLAYERS.FULLNAME,
            PLAYERS.EXPANDEDFULLNAME,
            PLAYERS.ACTIVEUNTIL,
        ).from(PLAYERS)
            .where(PLAYERS.ID.eq(id.id))
            .fetch()


        if (records.isEmpty() || records.size > 1) {
            return PlayerIdError("Unable to find player", id.id).left()
        }

        val record = records[0]

        val biography = PlayerBiography(
            fullName = record.get("FullName", String::class.java),
            expandedFullName = record.get("ExpandedFullName", String::class.java),
            activeUntil = record.get("ActiveUntil", Long::class.java),
            birthDate = record.getValue("BirthdateAsText", String::class.java) ?: "",
            dateDied = record.getValue("DateDiedAsText", String::class.java) ?: "",
            battingHand = getBattingHand(record.getValueOrNull("BattingHand", String::class.java)),
            bowlingMode = record.getValue("ShortBowlingStyles", String::class.java) ?: "",
        )


        return PlayerBiographyDto(biography).right()
    }

    /**
     * Determines the batting hand (e.g., right-handed, left-handed) based on the provided integer value as a string.
     *
     * @param battingHandAsInt The batting hand represented as a nullable string.
     *                         "1" corresponds to "Right handed bat",
     *                         "2" corresponds to "Left handed bat".
     *                         Any other value or null returns "unknown".
     * @return A string describing the batting hand ("Right handed bat", "Left handed bat", or "unknown").
     */
    private fun getBattingHand(battingHandAsInt: String?): String {
        return when (battingHandAsInt) {
            "1" -> "RHB"
            "2" -> "LHB"
            else -> "unknown"
        }
    }
}