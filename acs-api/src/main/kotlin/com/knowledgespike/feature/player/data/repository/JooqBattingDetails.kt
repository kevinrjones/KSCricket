package com.knowledgespike.feature.player.data.repository

import arrow.core.Either
import arrow.core.right
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.db.tables.references.BATTINGDETAILS
import com.knowledgespike.db.tables.references.GROUNDS
import com.knowledgespike.db.tables.references.MATCHES
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.player.domain.model.BattingDetailsDto
import kotlinx.datetime.LocalDate
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.iif

/**
 * Object that provides functionality to retrieve and process batting details of players.
 */
object JooqBattingDetails {

    /**
     * Retrieves the batting details for a player and organizes them by match type.
     * The data is fetched from a database and transformed into a structured format.
     *
     * @param context the DSLContext instance for executing database queries
     * @param id the unique player identifier of type PlayerId
     * @return an Either instance that represents either an Error if something goes wrong,
     *         or a Map containing match type as keys and lists of BattingDetailsDto as values
     */
    fun getBattingDetails(context: DSLContext, id: PlayerId): Either<Error, Map<String, List<BattingDetailsDto>>> {

        val battingRecords = context.select(
            BATTINGDETAILS.PLAYERID,
            MATCHES.CAID.`as`("matchId"),
            MATCHES.MATCHTITLE,
            MATCHES.MATCHTYPE,
            field("T.Name").`as`("Team"),
            field("O.Name").`as`("Opponents"),
            GROUNDS.KNOWNAS.`as`("Ground"),
            MATCHES.MATCHSTARTDATE,
            BATTINGDETAILS.INNINGSNUMBER,
            BATTINGDETAILS.TEAMID,
            BATTINGDETAILS.DISMISSAL,
            BATTINGDETAILS.DISMISSALTYPE,
            BATTINGDETAILS.FIELDERID,
            BATTINGDETAILS.FIELDERNAME,
            BATTINGDETAILS.BOWLERID,
            BATTINGDETAILS.BOWLERNAME,
            BATTINGDETAILS.SCORE,
            BATTINGDETAILS.POSITION,
            BATTINGDETAILS.NOTOUT,
            BATTINGDETAILS.BALLS,
            BATTINGDETAILS.MINUTES,
            BATTINGDETAILS.FOURS,
            BATTINGDETAILS.SIXES,
            BATTINGDETAILS.CAPTAIN,
            BATTINGDETAILS.WICKETKEEPER,
            iif(BATTINGDETAILS.BALLS.eq(0), 0, BATTINGDETAILS.SCORE.div(BATTINGDETAILS.BALLS).mul(100)).`as`("sr")
        ).from(BATTINGDETAILS)
            .join(TEAMS.`as`("T")).on(BATTINGDETAILS.TEAMID.eq(field("T.ID", Int::class.java)))
            .join(TEAMS.`as`("O")).on(BATTINGDETAILS.OPPONENTSID.eq(field("O.ID", Int::class.java)))
            .join(MATCHES).on(BATTINGDETAILS.MATCHID.eq(MATCHES.ID))
            .join(GROUNDS).on(BATTINGDETAILS.GROUNDID.eq(GROUNDS.ID))
            .where(BATTINGDETAILS.PLAYERID.eq(id.id))
            .orderBy(BATTINGDETAILS.MATCHTYPE, MATCHES.MATCHSTARTDATEASOFFSET, BATTINGDETAILS.INNINGSNUMBER)
            .fetch()



        val playerBatting = mutableListOf<BattingDetailsDto>()

        battingRecords.forEach { record ->
            playerBatting.add(
                BattingDetailsDto(
                    playerId = record.get("PlayerId", Int::class.java),
                            matchId = record.get("matchId", String::class.java),
                            matchType = record.get("MatchType", String::class.java),
                            matchTitle = record.get("MatchTitle", String::class.java),
                            team = record.get("Team", String::class.java),
                            opponents = record.get("Opponents", String::class.java),
                            ground = record.get("Ground", String::class.java),
                            matchStartDate = LocalDate.parse(record.get("MatchStartDate", String::class.java)),
                            inningsNumber = record.get("InningsNumber", Int::class.java),
                            dismissal = record.get("Dismissal", String::class.java),
                            dismissalType = record.get("DismissalType", Int::class.java),
                            fielderId = record.get("FielderId", Int::class.java),
                            fielderName = record.getValueOrNull("FielderName", String::class.java) ?: "",
                            bowlerId = record.get("BowlerId", Int::class.java),
                            bowlerName = record.getValueOrNull("BowlerName", String::class.java) ?: "",
                            score = record.get("Score", Int::class.java),
                            position = record.get("Position", Int::class.java),
                            notOut = record.get("NotOut", Boolean::class.java),
                            balls = record.get("Balls", Int::class.java),
                            minutes = record.get("Minutes", Int::class.java),
                            fours = record.get("Fours", Int::class.java),
                            sixes = record.get("Sixes", Int::class.java),
                            captain = record.get("Captain", Boolean::class.java),
                            wicketKeeper = record.get("WicketKeeper", Boolean::class.java),
                            sr = record.get("sr", Double::class.java),
                )
            )
        }

        return convertBattingDetailsToByMatchType(playerBatting)
    }

    /**
     * Converts a list of BattingDetailsDto objects into a map categorized by their match type.
     *
     * @param battingDetailsDtos A list of BattingDetailsDto objects to be organized by match type.
     * @return Either an Error if the operation fails, or a Map where the key is the match type and the value is a list of BattingDetailsDto objects for that match type.
     */
    private fun convertBattingDetailsToByMatchType(battingDetailsDtos: List<BattingDetailsDto>):
        Either<Error, Map<String, List<BattingDetailsDto>>> {

        val dict = mutableMapOf<String, MutableList<BattingDetailsDto>>()
        for (dto in battingDetailsDtos) {
            dict[dto.matchType]?.add(dto) ?: dict.put(dto.matchType, mutableListOf(dto))
        }
        return dict.right()
    }


}