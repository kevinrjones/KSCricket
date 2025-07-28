package com.knowledgespike.feature.bowlingrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents the bowling performance of a player inning by inning.
 *
 * This data class holds detailed statistics and contextual information about a
 * player's bowling performance in a specific match, including balls bowled, maidens,
 * runs conceded, and wickets taken. It also contains metadata such as player ID,
 * match details, and opponent information.
 *
 * Implements `IToCsv` for CSV serialization and `IToXls` for exporting data to
 * Excel worksheets, providing methods to handle headers and individual rows.
 */
@Serializable
data class BowlingInningsByInnings(
    val playerId: Int,
    val matchId: String,
    val fullName: String,
    val sortNamePart: String,
    val team: String,
    val opponents: String,
    val inningsNumber: Int,
    val ground: String,
    val matchDate: String,
    val playerBalls: Int,
    val playerMaidens: Int?,
    val playerDots: Int?,
    val playerRuns: Int,
    val playerWickets: Int,
    val ballsPerOver: Int,
    val econ: String

) : IToCsv, IToXls{
    /**
     * Converts the details of a bowling innings into a CSV-formatted string, with fields separated by the specified delimiter.
     *
     * @param separator The string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the bowling innings details.
     */
    override fun toCsv(separator: String): String {
        return "$fullName $separator $team $separator $opponents $separator $matchDate $separator $ground $separator $matchDate$separator" +
                "$inningsNumber $separator $playerBalls $separator $playerMaidens $separator $playerRuns $separator $playerWickets $separator $ballsPerOver"
    }

    /**
     * Generates the CSV header row for bowling innings statistics.
     *
     * @param separator The delimiter to use between each column in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Name $separator Team $separator Opponents $separator Date $separator Ground $separator Match Date $separator Innings $separator Balls $separator Maidens $separator Runs $separator Wickets $separator Balls Per Over"
    }

    /**
     * Populates the first row of the given worksheet with predefined column headers
     * for bowling statistics such as "FullName," "SortNamePart," "Team," and others.
     *
     * @param worksheet The sheet in which the header row will be created. If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        val row = worksheet?.createRow(0)

        row?.createCellAndAddValue(1,"FullName")
        row?.createCellAndAddValue(2,"SortNamePart")
        row?.createCellAndAddValue(3,"Team")
        row?.createCellAndAddValue(4,"Opponents")
        row?.createCellAndAddValue(5,"Ground")
        row?.createCellAndAddValue(6,"Match Date")
        row?.createCellAndAddValue(7,"Innings")
        row?.createCellAndAddValue(8,"Balls")
        row?.createCellAndAddValue(9,"Maidens")
        row?.createCellAndAddValue(10,"Runs")
        row?.createCellAndAddValue(11,"Wickets")
        row?.createCellAndAddValue(12,"Balls Per Over")
    }

    /**
     * Adds a new line to the specified worksheet by creating a new row at the given index and populating it
     * with player and match-related data.
     *
     * @param worksheet The worksheet where the new row will be added. If null, no operation will be performed.
     * @param index The index at which the new row should be created.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        val row = worksheet?.createRow(index)

        row?.createCellAndAddValue(1, fullName)
        row?.createCellAndAddValue(2, sortNamePart)
        row?.createCellAndAddValue(3, team)
        row?.createCellAndAddValue(4, opponents)
        row?.createCellAndAddValue(5, ground)
        row?.createCellAndAddValue(6, matchDate)
        row?.createCellAndAddValue(7, inningsNumber)
        row?.createCellAndAddValue(8, playerBalls)
        if(playerMaidens == null)
            row?.createCellAndAddValue(9, "-")
        else
            row?.createCellAndAddValue(9, playerMaidens)
        row?.createCellAndAddValue(10, playerRuns)
        row?.createCellAndAddValue(11, playerWickets)
        row?.createCellAndAddValue(12, ballsPerOver)

    }
}