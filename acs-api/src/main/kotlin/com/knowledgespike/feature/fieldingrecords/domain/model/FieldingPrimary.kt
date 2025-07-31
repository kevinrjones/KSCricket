package com.knowledgespike.feature.fieldingrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet


/**
 * Represents the primary fielding statistics of a cricket player.
 *
 * This data class is an entity to store detailed information about a player's fielding
 * performance over a specified year. It includes details such as the number of matches,
 * dismissals, and various specific fielding statistics like catches and stumpings.
 * Additionally, it provides capabilities to serialize its data into CSV and Excel-compatible formats.
 *
 * @property playerId Unique identifier of the player.
 * @property name Name of the player.
 * @property team Name of the team the player represents.
 * @property opponents The opposing teams the player competed against.
 * @property year The year in which the performance data was recorded.
 * @property matches The total number of matches played.
 * @property innings The total number of innings fielded. Nullable to represent absence of data.
 * @property ground The venue where the statistics were recorded.
 * @property countryName The country of origin or representation of the player.
 * @property dismissals Total number of dismissals by the player.
 * @property wicketKeeperDismissals Total number of dismissals performed by the player as a wicketkeeper.
 * @property caught Total number of catches taken by the player.
 * @property stumpings Total number of stumpings performed by the player.
 * @property caughtKeeper Catches taken by the player as a wicketkeeper.
 * @property caughtFielder Catches taken by the player as a fielder (non-wicketkeeper).
 * @property bestDismissals The highest number of dismissals in a single match.
 * @property bestCaughtFielder The highest number of catches taken as a fielder in a single match.
 * @property bestCaughtKeeper The highest number of catches taken as a wicketkeeper in a single match.
 * @property bestStumpings The highest number of stumpings in a single match.
 */
@Serializable
data class FieldingPrimary(
    val playerId: Int,
    val name: String,
    val debut: String,
    val end: String,
    val team: String,
    val opponents: String,
    val year: String,
    val matches: Int,
    val innings: Int?,
    val ground: String,
    val countryName: String,
    val dismissals: Int,
    val wicketKeeperDismissals: Int,
    val caught: Int,
    val stumpings: Int,
    val caughtKeeper: Int,
    val caughtFielder: Int,
    val bestDismissals: Int,
    val bestCaughtFielder: Int,
    val bestCaughtKeeper: Int,
    val bestStumpings: Int
): IToCsv, IToXls {
    /**
     * Generates a CSV header row containing column names for fielding statistics.
     *
     * @param separator The delimiter to use between column names in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Name $separator Team $separator Opponents $separator Year $separator Matches $separator Innings $separator Ground $separator Country $separator " +
                "Dismissals $separator WicketKeeper Dismissals $separator Caught $separator Stumped $separator Caught by Keeper $separator " +
                "Caught by Fielder $separator Best Dismissals $separator Best Caught Fielder $separator Best Caught Keeper $separator Best Stumpings"
    }

    /**
     * Converts the fielding primary statistics into a CSV formatted string, with values
     * separated by the specified delimiter.
     *
     * @param separator The character or string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the fielding primary statistics.
     */
    override fun toCsv(separator: String): String {
        return "$name $separator $team $separator $opponents $separator $year $separator $matches $separator $innings $separator $ground $separator $countryName $separator " +
                "$dismissals  $separator $wicketKeeperDismissals $separator $caught $separator $stumpings $separator $caughtKeeper $separator " +
                "$caughtFielder $separator $bestDismissals $separator $bestCaughtFielder $separator $bestCaughtKeeper $separator $bestStumpings"
    }

    /**
     * Adds a header row to the specified worksheet with predefined column titles related to fielding statistics.
     *
     * Each cell in the header row corresponds to a specific attribute, such as player name,
     * team, opponents, year, matches, innings, and various fielding metrics.
     *
     * @param worksheet The worksheet in which the header row will be created. If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Name")
            createCellAndAddValue(2, "Team")
            createCellAndAddValue(3, "Opponents")
            createCellAndAddValue(4, "Year")
            createCellAndAddValue(5, "Matches")
            createCellAndAddValue(6, "Innings")
            createCellAndAddValue(7, "Ground")
            createCellAndAddValue(8, "Country")
            createCellAndAddValue(9, "Dismissals")
            createCellAndAddValue(10, "WicketKeeper Dismissals")
            createCellAndAddValue(11, "Caught")
            createCellAndAddValue(12, "Stumpings")
            createCellAndAddValue(13, "Caught Keeper")
            createCellAndAddValue(14, "Caught Fielder")
            createCellAndAddValue(15, "Best Dismissals")
            createCellAndAddValue(16, "Best Caught Fielder")
            createCellAndAddValue(17, "Best Caught Keeper")
            createCellAndAddValue(18, "Best Stumpings")
        }
    }

    /**
     * Populates a row in the provided worksheet at the specified index with fielding-related data.
     *
     * @param worksheet The worksheet where the row needs to be created. If null, the operation is skipped.
     * @param index The index of the row to be added in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(index)?.apply {
            createCellAndAddValue(1, name)
            createCellAndAddValue(2, team)
            createCellAndAddValue(3, opponents)
            createCellAndAddValue(4, year)
            createCellAndAddValue(5, matches.toDouble())
            if(innings == null)
                createCellAndAddValue(6, "-")
            else
                createCellAndAddValue(6, innings.toDouble())
            createCellAndAddValue(7, ground)
            createCellAndAddValue(8, countryName)
            createCellAndAddValue(9, dismissals.toDouble())
            createCellAndAddValue(10, wicketKeeperDismissals.toDouble())
            createCellAndAddValue(11, caught.toDouble())
            createCellAndAddValue(12, stumpings.toDouble())
            createCellAndAddValue(13, caughtKeeper.toDouble())
            createCellAndAddValue(14, caughtFielder.toDouble())
            createCellAndAddValue(15, bestDismissals.toDouble())
            createCellAndAddValue(16, bestCaughtFielder.toDouble())
            createCellAndAddValue(17, bestCaughtKeeper.toDouble())
            createCellAndAddValue(18, bestStumpings.toDouble())
        }
    }
}
