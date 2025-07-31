package com.knowledgespike.feature.battingrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents a record of a player's batting performance in a cricket match, tracked innings by innings.
 *
 * This data class captures details such as the player's performance, match details, and other contextual information.
 * It implements the `IToCsv` and `IToXls` interfaces to facilitate exporting data in CSV and Excel formats respectively.
 *
 * @property playerId The unique identifier for the player.
 * @property matchId The unique identifier for the match.
 * @property fullName The full name of the player.
 * @property sortNamePart A short format of the player's name or surname for sorting or display purposes.
 * @property team The name of the team the player belongs to.
 * @property opponents The name of the opposing team in the match.
 * @property inningsNumber The specific innings number in the match.
 * @property ground The name of the ground where the match was played.
 * @property matchDate The date of the match.
 * @property playerScore The score made by the player in the innings.
 * @property bat1 The first set of batting data, if applicable.
 * @property bat2 The second set of batting data, if applicable.
 * @property notOut A flag indicating whether the player remained not out.
 * @property position The batting position of the player.
 * @property fours The number of boundary fours hit by the player.
 * @property balls The number of balls faced by the player.
 * @property sixes The number of boundary sixes hit by the player.
 * @property minutes The time spent by the player batting, in minutes.
 * @property sr The strike rate of the player during the innings.
 */
@Serializable
data class BattingInningsByInnings(
    val playerId: Int,
    val matchId: String,
    val fullName: String,
    val sortNamePart: String,
    val team: String,
    val opponents: String,
    val inningsNumber: Int = 0,
    val ground: String,
    val matchDate: String,
    val playerScore: Int,
    val bat1: Int? = 0,
    val bat2: Int? = 0,
    val notOut: Int? = 0,
    val position: Int = 0,
    val fours: Int,
    val balls: Int,
    val sixes: Int,
    val minutes: Int,
    val sr: Double,
) : IToCsv, IToXls {
    /**
     * Converts the batting innings data into a CSV formatted string using the provided separator.
     *
     * @param separator The character or string used to separate the fields in the CSV output.
     * @return A CSV-formatted string representing the batting innings data.
     */
    override fun toCsv(separator: String): String {
        return "$fullName $separator $sortNamePart $separator $team $separator $opponents $separator $ground $separator " +
                "$matchDate $separator $playerScore $separator $notOut $separator $position $separator $inningsNumber|" +
                "$balls $separator $fours $separator $sixes $separator $minutes"
    }

    /**
     * Generates the CSV header row for batting innings data.
     *
     * @param separator The character or string used to separate values in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Name $separator SortNamePart $separator Team $separator Opponents $separator Ground $separator Date $separator Score $separator Not Out $separator Innings  $separator Position $separator Balls $separator Fours $separator Sixes $separator Minutes"
    }

    /**
     * Adds a header row with predefined column titles to the specified worksheet.
     * Each cell in the header row corresponds to a specific aspect of batting data.
     *
     * @param worksheet The sheet in which the header row is to be created. If null,
     * no header row will be added.
     */
    override fun addHeader(worksheet: Sheet?) {
        val row = worksheet?.createRow(0)

        row?.createCellAndAddValue(1, "Name")
        row?.createCellAndAddValue(2, "SortNamePart")
        row?.createCellAndAddValue(3, "Team")
        row?.createCellAndAddValue(4, "Opponents")
        row?.createCellAndAddValue(5, "Ground")
        row?.createCellAndAddValue(6, "Date")
        row?.createCellAndAddValue(7, "Score")
        row?.createCellAndAddValue(8, "Not Out")
        row?.createCellAndAddValue(9, "Innings")
        row?.createCellAndAddValue(10, "Position")
        row?.createCellAndAddValue(11, "Balls")
        row?.createCellAndAddValue(12, "Fours")
        row?.createCellAndAddValue(13, "Sixes")
        row?.createCellAndAddValue(14, "Minutes")
    }

    /**
     * Populates a new row in the specified worksheet at the given index with batting information.
     *
     * @param worksheet The worksheet where the row will be added. Can be null.
     * @param index The zero-based index at which the row will be created in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        val row = worksheet?.createRow(index)

        row?.createCellAndAddValue(1, fullName)
        row?.createCellAndAddValue(2, sortNamePart)
        row?.createCellAndAddValue(3, team)
        row?.createCellAndAddValue(4, opponents)
        row?.createCellAndAddValue(5, ground)
        row?.createCellAndAddValue(6, matchDate)
        row?.createCellAndAddValue(7, playerScore)
        if (notOut == null)
            row?.createCellAndAddValue(8, "-")
        else
            row?.createCellAndAddValue(8, notOut)
        row?.createCellAndAddValue(9, inningsNumber)
        row?.createCellAndAddValue(1, position)
        row?.createCellAndAddValue(1, balls)
        row?.createCellAndAddValue(1, fours)
        row?.createCellAndAddValue(1, sixes)
        row?.createCellAndAddValue(1, minutes)
    }
}