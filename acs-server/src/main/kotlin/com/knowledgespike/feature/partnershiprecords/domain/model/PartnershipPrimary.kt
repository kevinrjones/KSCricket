package com.knowledgespike.feature.partnershiprecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Data class representing the primary details of a cricket partnership.
 *
 * This class encapsulates information about a partnership in a cricket match such as player details,
 * their teams, the opponents, performance statistics (e.g., innings played, runs scored, averages, and notable scores),
 * and match-related metadata like ground, country, and series date.
 *
 * Implements `IToCsv` for CSV serialization and `IToXls` for Excel sheet data representation.
 *
 * @property playerIds String representing identifiers of the players in the partnership.
 * @property playerNames String representing the names of the players in the partnership.
 * @property player1 Name of the first player in the partnership.
 * @property player2 Name of the second player in the partnership.
 * @property player1Id Identifier for the first player.
 * @property player2Id Identifier for the second player.
 * @property team Name of the team the partnership belongs to.
 * @property opponents Name of the opposing team.
 * @property innings Number of innings the partnership played.
 * @property notOuts Number of not-out instances for the partnership.
 * @property runs Total runs scored by the partnership.
 * @property avg Average runs for the partnership, nullable.
 * @property hundreds Number of instances where the partnership scored 100 or more runs.
 * @property fifties Number of instances where the partnership scored 50-99 runs.
 * @property highestScore Highest runs scored by the partnership in a single inning.
 * @property unbroken Flag indicating whether the partnership remained unbroken.
 * @property ground Name of the ground where the partnership occurred.
 * @property countryName Name of the country where the partnership occurred.
 * @property seriesDate Date of the series in which the partnership occurred.
 */
@Serializable
data class PartnershipPrimary(
    val playerIds: String,
    val playerNames: String,
    val player1: String,
    val player2: String,
    val player1Id: Int,
    val player2Id: Int,
    val team: String,
    val opponents: String,
    val innings: Int,
    val notOuts: Int,
    val runs: Int,
    val avg: Double?,
    val hundreds: Int,
    val fifties: Int,
    val highestScore: Int,
    val unbroken: Boolean,
    val ground: String,
    val countryName: String,
    val seriesDate: String
): IToCsv, IToXls {
    /**
     * Converts the partnership's primary statistics into a CSV formatted string, with fields
     * separated by the specified delimiter.
     *
     * @param separator The character or string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the partnership's primary statistics.
     */
    override fun toCsv(separator: String): String {
        return "$playerNames $separator $player1 $separator $player2 $separator $team $separator $opponents " +
                "$separator $innings $separator $notOuts $separator $runs $separator $avg $separator $hundreds " +
                "$separator $fifties $separator $highestScore $separator $unbroken $separator $ground " +
                "$separator $countryName $separator $seriesDate"
    }

    /**
     * Generates the CSV header row for partnership-related statistics.
     *
     * @param separator The delimiter to use between each column in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Player Names $separator Player 1 $separator Player 2 $separator Team $separator Opponents " +
                "$separator Innings $separator Not Outs $separator Runs $separator Avg $separator Hundreds " +
                "$separator Fifties $separator Highest Score $separator Unbroken $separator Ground " +
                "$separator Country Name $separator Series Date"
    }

    /**
     * Adds a header row to the specified Excel worksheet with predefined column titles
     * related to player and partnership statistics.
     *
     * @param worksheet The Excel sheet where the header row will be created.
     * If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(0, "Player Names")
            createCellAndAddValue(1, "Player 1")
            createCellAndAddValue(2, "Player 2")
            createCellAndAddValue(3, "Team")
            createCellAndAddValue(4, "Opponents")
            createCellAndAddValue(5, "Innings")
            createCellAndAddValue(6, "Not Outs")
            createCellAndAddValue(7, "Runs")
            createCellAndAddValue(8, "Average")
            createCellAndAddValue(9, "Hundreds")
            createCellAndAddValue(10, "Fifties")
            createCellAndAddValue(11, "Highest Score")
            createCellAndAddValue(12, "Unbroken")
            createCellAndAddValue(13, "Ground")
            createCellAndAddValue(14, "Country")
        }
    }

    /**
     * Adds a line of data to the specified worksheet at the given row index.
     *
     * @param worksheet The worksheet where the row will be added. If null, the operation is skipped.
     * @param index The row index in the worksheet where the new line should be added.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(index)?.apply {
            createCellAndAddValue(0, playerNames)
            createCellAndAddValue(1, player1)
            createCellAndAddValue(2, player2)
            createCellAndAddValue(3, team)
            createCellAndAddValue(4, opponents)
            createCellAndAddValue(5, innings.toDouble())
            createCellAndAddValue(6, notOuts.toDouble())
            createCellAndAddValue(7, runs.toDouble())
            createCellAndAddValue(8, avg ?: 0.0)
            createCellAndAddValue(9, hundreds.toDouble())
            createCellAndAddValue(10, fifties.toDouble())
            createCellAndAddValue(11, highestScore.toDouble())
            createCellAndAddValue(12, unbroken)
            createCellAndAddValue(13, ground)
            createCellAndAddValue(14, countryName)
        }
    }
    
}
