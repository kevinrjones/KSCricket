package com.knowledgespike.feature.player.data.repository

import arrow.core.Either
import arrow.core.right
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.db.tables.references.BOWLINGDETAILS
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.MATCHES
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.player.domain.model.BowlingDetailsDto
import kotlinx.datetime.LocalDate
import org.jooq.DSLContext
import org.jooq.impl.DSL.field

/**
 * Object providing functionality to retrieve and process bowling details
 * for a specific player from the database.
 */
object JooqBowlingDetails {

    /**
     * Retrieves bowling details for a player based on their ID.
     *
     * @param context The DSLContext used for executing database queries.
     * @param id The unique identifier of the player whose bowling details need to be fetched.
     * @return Either an Error object if an issue occurs, or a Map where the key is the match type
     *         and the value is a list of bowling details corresponding to that match type.
     */
    fun getBowlingDetails(context: DSLContext, id: PlayerId): Either<Error, Map<String, List<BowlingDetailsDto>>> {

        val bowlingRecords = context.select(
            BOWLINGDETAILS.PLAYERID,
            MATCHES.CAID.`as`("matchId"),
            MATCHES.MATCHTITLE,
            MATCHES.MATCHTYPE,
            field("T.Name").`as`("Team"),
            field("O.Name").`as`("Opponents"),
            GROUNDS.KNOWNAS.`as`("Ground"),
            MATCHES.MATCHSTARTDATE,
            BOWLINGDETAILS.INNINGSNUMBER,
            BOWLINGDETAILS.RUNS,
            BOWLINGDETAILS.BALLS,
            BOWLINGDETAILS.WICKETS,
            BOWLINGDETAILS.MAIDENS,
            BOWLINGDETAILS.DOTS,
            BOWLINGDETAILS.NOBALLS,
            BOWLINGDETAILS.WIDES,
            BOWLINGDETAILS.FOURS,
            BOWLINGDETAILS.SIXES,
            BOWLINGDETAILS.CAPTAIN,
            MATCHES.BALLSPEROVER
        ).from(BOWLINGDETAILS)
            .join(TEAMS.`as`("T")).on(BOWLINGDETAILS.TEAMID.eq(field("T.ID", Int::class.java)))
            .join(TEAMS.`as`("O")).on(BOWLINGDETAILS.OPPONENTSID.eq(field("O.ID", Int::class.java)))
            .join(MATCHES).on(BOWLINGDETAILS.MATCHID.eq(MATCHES.ID))
            .join(GROUNDS).on(BOWLINGDETAILS.GROUNDID.eq(GROUNDS.ID))
            .where(BOWLINGDETAILS.PLAYERID.eq(id.id))
            .orderBy(BOWLINGDETAILS.MATCHTYPE, MATCHES.MATCHSTARTDATEASOFFSET, BOWLINGDETAILS.INNINGSNUMBER)
            .fetch()


        val playerBowling = mutableListOf<BowlingDetailsDto>()

        bowlingRecords.forEach { record ->
            playerBowling.add(
                BowlingDetailsDto(
                    playerId = record.get("PlayerId", Int::class.java),
                    matchId = record.get("matchId", String::class.java),
                    matchType = record.get("MatchType", String::class.java),
                    matchTitle = record.get("MatchTitle", String::class.java),
                    team = record.get("Team", String::class.java),
                    opponents = record.get("Opponents", String::class.java),
                    ground = record.get("Ground", String::class.java),
                    matchStartDate = LocalDate.parse(record.get("MatchStartDate", String::class.java)),
                    inningsNumber = record.get("InningsNumber", Int::class.java),
                    runs = record.get("Runs", Int::class.java),
                    balls = record.get("Balls", Int::class.java),
                    wickets = record.get("Wickets", Int::class.java),
                    maidens = record.get("Maidens", Int::class.java),
                    dots = record.get("Dots", Int::class.java),
                    noBalls = record.get("NoBalls", Int::class.java),
                    wides = record.get("Wides", Int::class.java),
                    fours = record.get("Fours", Int::class.java),
                    sixes = record.get("Sixes", Int::class.java),
                    captain = record.get("Captain", Boolean::class.java),
                    ballsPerOver = record.get("BallsPerOver", Int::class.java),
                )
            )
        }

        return convertBowlingDetailsToByMatchType(playerBowling)
    }

    /**
     * Converts a list of bowling details into a map grouped by match type.
     *
     * Processes the given list of [BowlingDetailsDto] and organizes them into a map
     * where the keys are match types and the values are lists of BowlingDetailsDto corresponding to that match type.
     * If no match types are found, an empty map is returned wrapped in an `Either.Right`.
     *
     * @param bowlingDetailsDtos The list of BowlingDetailsDto to be grouped by match type.
     * @return Either an Error if an operation fails, or a map where the key is the match type
     *         and the value is a list of BowlingDetailsDto belonging to that match type.
     */
    private fun convertBowlingDetailsToByMatchType(bowlingDetailsDtos: List<BowlingDetailsDto>):
            Either<Error, Map<String, List<BowlingDetailsDto>>> {

        val dict = mutableMapOf<String, MutableList<BowlingDetailsDto>>()
        for (dto in bowlingDetailsDtos) {
            dict[dto.matchType]?.add(dto) ?: dict.put(dto.matchType, mutableListOf(dto))
        }
        return dict.right()
    }


}