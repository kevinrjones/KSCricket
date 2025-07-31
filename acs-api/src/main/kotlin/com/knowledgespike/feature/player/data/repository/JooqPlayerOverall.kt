package com.knowledgespike.feature.player.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.error.PlayerIdError
import com.knowledgespike.core.type.values.PlayerId
import com.knowledgespike.db.tables.references.BATTINGDETAILS
import com.knowledgespike.db.tables.references.BOWLINGDETAILS
import com.knowledgespike.db.tables.references.TEAMS
import com.knowledgespike.feature.player.domain.model.PlayerBattingOverallDto
import com.knowledgespike.feature.player.domain.model.PlayerBowlingOverallDto
import com.knowledgespike.feature.player.domain.model.PlayerOverallDto
import org.jooq.DSLContext
import org.jooq.impl.DSL.*

/**
 * The `JooqPlayerOverall` object provides utility functions to retrieve and analyze
 * overall player statistics, combining and organizing batting and bowling data
 * into a cohesive structure.
 */
object JooqPlayerOverall {

    /**
     * Retrieves the player's overall performance data by combining batting and bowling statistics
     * based on match type and team, and organizes the information into a nested list structure.
     *
     * @param context the database context for executing queries
     * @param id the unique identifier of the player whose performance data is to be retrieved
     * @return either a PlayerIdError if the player data cannot be found, or a nested list of PlayerOverallDto
     *         containing the player's aggregated batting and bowling statistics
     */
    fun getOverall(context: DSLContext, id: PlayerId): Either<PlayerIdError, List<List<PlayerOverallDto>>> {

        val battingRecords = context.select(
            BATTINGDETAILS.MATCHTYPE,
            BATTINGDETAILS.TEAMID,
            field("T.Name").`as`("Team"),
            count(
                `when`(
                    BATTINGDETAILS.INNINGSNUMBER.eq(1),
                    1
                )
            ).`as`("matches"),
            count(
                `when`(
                    BATTINGDETAILS.DISMISSALTYPE.ne(11).and(BATTINGDETAILS.DISMISSALTYPE.ne(14)),
                    1
                )
            ).`as`("innings"),
            sum(coalesce(BATTINGDETAILS.SCORE, 0)).`as`("runs"),
            sum(coalesce(BATTINGDETAILS.NOTOUT, 0)).`as`("notouts"),
            sum(coalesce(BATTINGDETAILS.BALLS, 0)).`as`("balls"),
            sum(coalesce(BATTINGDETAILS.FOURS, 0)).`as`("fours"),
            sum(coalesce(BATTINGDETAILS.SIXES, 0)).`as`("sixes"),
            sum(BATTINGDETAILS.HUNDRED).`as`("hundreds"),
            sum(BATTINGDETAILS.FIFTY).`as`("fifties"),
            max(BATTINGDETAILS.NOTOUTADJUSTEDSCORE).`as`("highestscore"),
        ).from(BATTINGDETAILS)
            .join(TEAMS.`as`("T")).on(BATTINGDETAILS.TEAMID.eq(field("T.ID", Int::class.java)))
            .where(BATTINGDETAILS.PLAYERID.eq(id.id))
            .groupBy(BATTINGDETAILS.MATCHTYPE, BATTINGDETAILS.TEAMID)
            .fetch()


        val bowlingRecords = context.select(
            BOWLINGDETAILS.MATCHTYPE,
            BOWLINGDETAILS.TEAMID,
            count().`as`("matches"),
            sum(coalesce(BOWLINGDETAILS.BALLS, 0)).`as`("balls"),
            sum(coalesce(BOWLINGDETAILS.MAIDENS, 0)).`as`("maidens"),
            sum(coalesce(BOWLINGDETAILS.WICKETS, 0)).`as`("wickets"),
            sum(coalesce(BOWLINGDETAILS.FOURS, 0)).`as`("fours"),
            sum(coalesce(BOWLINGDETAILS.SIXES, 0)).`as`("sixes"),
            sum(coalesce(BOWLINGDETAILS.RUNS, 0)).`as`("runs"),
            sum(coalesce(BOWLINGDETAILS.WIDES, 0)).`as`("wides"),
            sum(coalesce(BOWLINGDETAILS.NOBALLS, 0)).`as`("noballs"),
        ).from(BOWLINGDETAILS)
            .where(BOWLINGDETAILS.PLAYERID.eq(id.id))
            .groupBy(BOWLINGDETAILS.MATCHTYPE, BOWLINGDETAILS.TEAMID)
            .fetch()

        val playerBatting = mutableListOf<PlayerBattingOverallDto>()
        val playerBowling = mutableListOf<PlayerBowlingOverallDto>()

        battingRecords.forEach { record ->
            playerBatting.add(
                PlayerBattingOverallDto(
                    team = record.get("Team", String::class.java),
                    matchType = record.get("MatchType", String::class.java),
                    teamId = record.get("TeamId", Int::class.java),
                    matches = record.get("matches", Int::class.java),
                    runs = record.get("runs", Int::class.java),
                    innings = record.get("innings", Int::class.java),
                    notouts = record.get("notouts", Int::class.java),
                    balls = record.get("balls", Int::class.java),
                    fours = record.get("fours", Int::class.java),
                    sixes = record.get("sixes", Int::class.java),
                    hundreds = record.get("hundreds", Int::class.java),
                    fifties = record.get("fifties", Int::class.java),
                    highestScore = record.get("highestscore", Double::class.java),
                )
            )
        }

        bowlingRecords.forEach { record ->
            playerBowling.add(
                PlayerBowlingOverallDto(
                    matchType = record.get("MatchType", String::class.java),
                    teamId = record.get("TeamId", Int::class.java),
                    matches = record.get("matches", Int::class.java),
                    bowlingBalls = record.getValueOrNull("balls", Int::class.java)?.toInt(),
                    bowlingRuns = record.getValueOrNull("runs", Int::class.java)?.toInt(),
                    maidens = record.getValueOrNull("maidens", Int::class.java)?.toInt(),
                    wickets = record.getValueOrNull("wickets", Int::class.java)?.toInt(),
                    bowlingFours = record.getValueOrNull("fours", Int::class.java)?.toInt(),
                    bowlingSixes = record.getValueOrNull("sixes", Int::class.java)?.toInt(),
                    wides = record.getValueOrNull("wides", Int::class.java)?.toInt(),
                    noBalls = record.getValueOrNull("noballs", Int::class.java)?.toInt(),
                )
            )
        }

        val playerOverallDto = mergeOverall(playerBatting, playerBowling)

        if (playerOverallDto.isEmpty()) {
            return PlayerIdError("Unable to find player", id.id).left()
        }


        return sortOverallIntoCollection(playerOverallDto).right()
    }

    /**
     * Merges batting and bowling statistics for players and returns a consolidated list.
     *
     * @param resultBatting A list of PlayerBattingOverallDto containing batting statistics for players.
     * @param resultBowling A list of PlayerBowlingOverallDto containing bowling statistics for players.
     * @return A list of PlayerOverallDto combining batting and bowling statistics for each player.
     */
    private fun mergeOverall(
        resultBatting: List<PlayerBattingOverallDto>,
        resultBowling: List<PlayerBowlingOverallDto>
    ): List<PlayerOverallDto> {
        val result = mutableListOf<PlayerOverallDto>()
        resultBatting.forEach { battingOverallDto ->
            var bowling = resultBowling
                .filter { b -> b.teamId == battingOverallDto.teamId && b.matchType == battingOverallDto.matchType }
                .firstOrNull()

            if (bowling == null)
                bowling = PlayerBowlingOverallDto(
                    battingOverallDto.matchType, battingOverallDto.teamId,
                    battingOverallDto.matches, null, null, null,
                    null, null, null, null, null
                )


            val playerOverallDto = PlayerOverallDto(
                battingOverallDto.team,
                battingOverallDto.matchType,
                battingOverallDto.teamId,
                battingOverallDto.matches,
                battingOverallDto.runs,
                battingOverallDto.innings,
                battingOverallDto.notouts,
                battingOverallDto.balls,
                battingOverallDto.fours,
                battingOverallDto.sixes,
                battingOverallDto.hundreds,
                battingOverallDto.fifties,
                battingOverallDto.highestScore,
                bowling.bowlingBalls,
                bowling.bowlingRuns,
                bowling.maidens,
                bowling.wickets,
                bowling.bowlingFours,
                bowling.bowlingSixes,
                bowling.wides,
                bowling.noBalls
            )
            result.add(playerOverallDto)
        }

        return result
    }

    /**
     * Sorts and groups a given list of `PlayerOverallDto` objects into a collection based on predefined match types.
     * Each group contains all players of the same match type, followed by a summary of totals for that match type.
     *
     * @param originalList The list of `PlayerOverallDto` objects representing individual player statistics.
     * @return A list of lists, where each inner list represents a group of players with the same match type,
     *         followed by the total statistics for that group.
     */
    private fun sortOverallIntoCollection(originalList: List<PlayerOverallDto>): List<List<PlayerOverallDto>> {
        val listOfSortedMatchType = listOf(
            "wt", "wo", "witt", "wf", "wa", "wtt", "t", "o", "itt", "f", "a", "tt", "sec"
        )

        val sortedList = mutableListOf<List<PlayerOverallDto>>()

        for (matchType in listOfSortedMatchType) {
            val innerList = mutableListOf<PlayerOverallDto>()
            val listOfMatchType = originalList.filter { it.matchType == matchType }

            var totals: PlayerOverallDto? = null
            if (listOfMatchType.isNotEmpty()) {
                totals = listOfMatchType.reduce { a, b ->
                    a.copy(
                        matches = a.matches + (b.matches),
                        runs = a.runs + (b.runs),
                        innings = a.innings + (b.innings),
                        notouts = a.notouts + (b.notouts),
                        balls = a.balls + (b.balls),
                        fours = a.fours + (b.fours),
                        sixes = a.sixes + (b.sixes),
                        hundreds = a.hundreds + (b.hundreds),
                        fifties = a.fifties + (b.fifties),
                        bowlingBalls = (a.bowlingBalls ?: 0) + (b.bowlingBalls ?: 0),
                        bowlingRuns = (a.bowlingRuns ?: 0) + (b.bowlingRuns ?: 0),
                        maidens = (a.maidens ?: 0) + (b.maidens ?: 0),
                        wickets = (a.wickets ?: 0) + (b.wickets ?: 0),
                        bowlingFours = (a.bowlingFours ?: 0) + (b.bowlingFours ?: 0),
                        bowlingSixes = (a.bowlingSixes ?: 0) + (b.bowlingSixes ?: 0),
                        wides = (a.wides ?: 0) + (b.wides ?: 0),
                        noBalls = (a.noBalls ?: 0) + (b.noBalls ?: 0)
                    )
                }

                totals = totals.copy(team = "Total")
            }

            innerList.addAll(originalList.filter { it.matchType == matchType })
            if (totals != null) {
                innerList.add(totals)
                sortedList.add(innerList)
            }
        }

        return sortedList
    }

}