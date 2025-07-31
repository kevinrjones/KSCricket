package com.knowledgespike.feature.fieldingrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Data class representing the fielding performance of a player on an innings-by-innings basis.
 *
 * Used to capture and serialize detailed fielding statistics for a player in a cricket match.
 * This class implements `IToCsv` and `IToXls` interfaces for exporting data to CSV and Excel respectively.
 *
 * @property playerId The unique identifier for the player.
 * @property matchId The identifier of the match where the performance occurred.
 * @property fullName The full name of the player.
 * @property team The name of the player's team.
 * @property opponents The name of the opposing team.
 * @property inningsNumber The innings number in which the performance occurred.
 * @property ground The ground where the match was played.
 * @property matchDate The date of the match.
 * @property dismissals Total number of dismissals made by the player during the innings.
 * @property wicketKeepingDismissals Number of dismissals made by the player as a wicket-keeper.
 * @property caught Total number of catches taken by the player.
 * @property stumpings Total number of stumpings made by the player.
 * @property caughtKeeper Number of catches taken by the player as a wicket-keeper.
 * @property caughtFielder Number of catches taken by the player as a fielder (non-wicket-keeper).
 */
@Serializable
data class FieldingInningsByInnings(
    val playerId: Int,
    val matchId: String,
    val fullName: String,
    val team: String,
    val opponents: String,
    val inningsNumber: Int,
    val ground: String,
    val matchDate: String,
    val dismissals: Int,
    val wicketKeepingDismissals: Int,
    val caught: Int,
    val stumpings: Int,
    val caughtKeeper: Int,
    val caughtFielder: Int
) : IToCsv, IToXls {
    /**
     * Converts the fielding innings data into a CSV-formatted string, where fields are separated
     * by the specified delimiter.
     *
     * @param separator The character or string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the fielding innings data.
     */
    override fun toCsv(separator: String): String {
        return "$fullName $separator $team $separator $inningsNumber $separator $opponents $separator $ground $separator $matchDate $separator $dismissals $separator " +
                "$wicketKeepingDismissals $separator $caught $separator $stumpings $separator $caughtFielder$caughtKeeper"
    }

    /**
     * Generates the CSV header row for fielding statistics.
     *
     * @param separator The delimiter used to separate column names in the header row.
     * @return A string representing the CSV header row with column names related to fielding statistics.
     */
    override fun csvHeader(separator: String): String {
        return "Name $separator Team $separator InningsNumber $separator Opponents $separator Ground $separator Date $separator Dismissals $separator " +
                "WicketKeeper Dismissals $separator Caught $separator Stumpings $separator Caught Fielder $separator Caught Keeper"
    }

    /**
     * Adds a header row to the specified worksheet with column titles relevant to fielding statistics.
     *
     * The header includes titles such as "Name," "Team," "Innings Number," and other attributes
     * to organize fielding performance data.
     *
     * @param worksheet The worksheet where the header row will be added. If the worksheet is null,
     *                  no action will be performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Name")
            createCellAndAddValue(2, "Team")
            createCellAndAddValue(3, "Innings Number")
            createCellAndAddValue(4, "Opponents")
            createCellAndAddValue(5, "Ground")
            createCellAndAddValue(6, "Date")
            createCellAndAddValue(7, "Dismissals")
            createCellAndAddValue(8, "WicketKeeper Dismissals")
            createCellAndAddValue(9, "Caught")
            createCellAndAddValue(10, "Stumpings")
            createCellAndAddValue(11, "Caught Fielder")
            createCellAndAddValue(12, "Caught Keeper")
        }

    }

    /**
     * Adds a row to the provided worksheet at the specified index, populating it with
     * fielding performance-related data such as player details, match details, and performance statistics.
     *
     * @param worksheet The worksheet where the new row should be added. If null, no row is added.
     * @param index The index at which the new row should be created in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(index)?.apply {
            createCellAndAddValue(1, fullName)
            createCellAndAddValue(2, team)
            createCellAndAddValue(3, inningsNumber.toDouble())
            createCellAndAddValue(4, opponents)
            createCellAndAddValue(5, ground)
            createCellAndAddValue(6, matchDate)
            createCellAndAddValue(7, dismissals.toDouble())
            createCellAndAddValue(8, wicketKeepingDismissals.toDouble())
            createCellAndAddValue(9, caught.toDouble())
            createCellAndAddValue(10, stumpings.toDouble())
            createCellAndAddValue(11, caughtFielder.toDouble())
            createCellAndAddValue(12, caughtKeeper.toDouble())
        }
    }

}
