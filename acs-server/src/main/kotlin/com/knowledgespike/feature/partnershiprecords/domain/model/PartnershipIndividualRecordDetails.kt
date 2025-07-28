package com.knowledgespike.feature.partnershiprecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents the details of a partnership record involving individual cricket players.
 *
 * This data class holds information about a specific partnership event in a cricket match, including
 * player details, match details, and performance statistics such as runs scored, wickets lost,
 * and whether the partnership remains unbroken. Additionally, it provides methods for
 * exporting this data to CSV or Excel formats by implementing `IToCsv` and `IToXls` interfaces.
 *
 * @property matchId Unique identifier for the match.
 * @property playerIds Comma-separated list of player identifiers involved in the partnership.
 * @property playerNames Names of the players involved in the partnership, separated by commas.
 * @property player1 Name of the first player in the partnership.
 * @property player2 Name of the second player in the partnership.
 * @property player1Id Unique identifier of the first player.
 * @property player2Id Unique identifier of the second player.
 * @property team Name of the team to which the partnership belongs.
 * @property opponents Name of the opposing team.
 * @property runs Number of runs scored in this partnership.
 * @property innings Innings during which the partnership occurred.
 * @property wicket Total wickets lost during this partnership.
 * @property currentScore Current score at the time of the partnership.
 * @property unbroken1 Indicates if the first player remains not out.
 * @property unbroken2 Indicates if the second player remains not out.
 * @property previousWicket Number of wickets lost prior to the partnership.
 * @property previousScore Team's score prior to the partnership.
 * @property ground Name of the ground where the match was played.
 * @property matchStartDate Start date of the match.
 * @property matchTitle The title or description of the match.
 * @property resultString Result of the match as a descriptive string.
 */
@Serializable
data class PartnershipIndividualRecordDetails(
    val matchId: String,
    val playerIds: String,
    val playerNames: String,
    val player1: String,
    val player2: String,
    val player1Id: Int,
    val player2Id: Int,
    val team: String,
    val opponents: String,
    val runs: Int,
    val innings: Int,
    val wicket: Int,
    val currentScore: Int,
    val unbroken1: Boolean,
    val unbroken2: Boolean,
    val previousWicket: Int,
    val previousScore: Int?,
    val ground: String,
    val matchStartDate: String,
    val matchTitle: String,
    val resultString: String
) : IToCsv, IToXls {
    /**
     * Converts the details of a partnership record into a CSV-formatted string.
     *
     * @param separator The character or string used to separate values in the resulting CSV string.
     * @return A CSV-formatted string representing the details of the partnership record, with each field separated by the provided separator.
     */
    override fun toCsv(separator: String): String {
        return "$runs | $wicket | $unbroken1| $playerNames | $team | $opponents | $matchStartDate | " +
                "$matchTitle | $ground | $resultString "
    }

    /**
     * Generates the CSV header row for partnership record details.
     *
     * @param separator The character or string used to separate values in the resulting CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Partnership | Wicket | Unbroken | Player Names | Team | Opponents | Start Date | Title | Ground | Result "
    }

    /**
     * Adds a header row with predefined column titles to the given worksheet.
     *
     * The header row contains titles related to partnership records, such as "Partnership,"
     * "Wicket," "Unbroken," "Player Names," "Team," "Opponents," "Start Date," "Title,"
     * "Ground," and "Result." Each title corresponds to a specific column.
     *
     * @param worksheet The worksheet to which the header row will be added. If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(0, "Partnership")
            createCellAndAddValue(1, "Wicket")
            createCellAndAddValue(2, "Unbroken")
            createCellAndAddValue(3, "Player Names")
            createCellAndAddValue(4, "Team")
            createCellAndAddValue(5, "Opponents")
            createCellAndAddValue(6, "Start Date")
            createCellAndAddValue(7, "Title")
            createCellAndAddValue(8, "Ground")
            createCellAndAddValue(9, "Result")
        }
    }

    /**
     * Adds a new line to the specified worksheet by creating a row at the given index
     * and populating it with data from the object's fields.
     *
     * @param worksheet The worksheet (Sheet) where the new row is to be added.
     *                  If null, no action is performed.
     * @param index The index at which the new row will be created in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(index)?.apply {
            createCellAndAddValue(0, runs.toDouble())
            createCellAndAddValue(1, wicket.toDouble())
            createCellAndAddValue(2, unbroken1)
            createCellAndAddValue(3, playerNames)
            createCellAndAddValue(4, team)
            createCellAndAddValue(5, opponents)
            createCellAndAddValue(6, matchStartDate)
            createCellAndAddValue(7, matchTitle)
            createCellAndAddValue(8, ground)
            createCellAndAddValue(9, resultString)
        }
    }
}
