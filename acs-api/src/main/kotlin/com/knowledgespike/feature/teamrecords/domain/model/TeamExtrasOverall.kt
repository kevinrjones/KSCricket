package com.knowledgespike.feature.teamrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents the overall extra statistics for a cricket team.
 *
 * This data class encapsulates various statistical details such as the number of matches played,
 * runs scored, extras conceded, and other specific details related to byes, leg byes, wides, etc.
 * It also implements functionality for exporting data in both CSV and Excel-compatible formats.
 *
 * Implements:
 * - IToCsv: Provides methods for serializing the data into a CSV format.
 * - IToXls: Provides methods for adding data into an Excel worksheet, including headers and rows.
 *
 * @property team The name of the team.
 * @property played The number of matches played by the team.
 * @property runs The total runs scored by the team.
 * @property extras The total extras conceded by the team.
 * @property byes The number of runs given as byes.
 * @property legByes The number of runs given as leg byes.
 * @property wides The number of wides conceded.
 * @property noBalls The number of no-balls conceded.
 * @property penalties The number of penalty runs conceded.
 * @property balls The total number of balls delivered by the team.
 * @property wickets The total number of wickets taken by the team.
 * @property percentage The percentage representation of extras out of total runs.
 */
@Serializable
data class TeamExtrasOverall(
    val team: String,
    val played: Int,
    val runs: Int,
    val extras: Int,
    val byes: Int,
    val legByes: Int,
    val wides: Int,
    val noBalls: Int,
    val penalties: Int,
    val balls: Int,
    val wickets: Int,
    val percentage: Double
) : IToCsv, IToXls {

    /**
     * Generates a string representing the CSV header for the team's overall performance metrics.
     *
     * @param separator The delimiter to be used between the column names in the CSV header.
     * @return A string containing the names of the columns separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Team $separator Played $separator Runs $separator Extras $separator Byes $separator Leg Byes $separator Wides $separator No Balls $separator Penalties $separator Balls $separator Wickets $separator Percentage"
    }

    /**
     * Converts the team's overall statistics into a CSV formatted string using the specified separator.
     *
     * @param separator The string used to separate values in the resulting CSV format.
     * @return A CSV-formatted string representing the team's overall statistics.
     */
    override fun toCsv(separator: String): String {
        return "$team $separator $played $separator $extras $separator $runs $separator $byes $separator $legByes $separator $wides $separator $noBalls $separator $penalties $separator $balls $separator $percentage"
    }

    /**
     * Adds a header row to the specified worksheet with predefined column titles.
     * Each cell in the header row represents a specific attribute related to team statistics.
     *
     * @param worksheet The worksheet where the header row will be added. If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Team")
            createCellAndAddValue(2, "Played")
            createCellAndAddValue(3, "Runs")
            createCellAndAddValue(4, "Extras")
            createCellAndAddValue(5, "Byes")
            createCellAndAddValue(6, "Leg Byes")
            createCellAndAddValue(7, "Wides")
            createCellAndAddValue(8, "No Balls")
            createCellAndAddValue(9, "Penalties")
            createCellAndAddValue(10, "Balls")
            createCellAndAddValue(11, "Wickets")
            createCellAndAddValue(12, "Percentage")
        }
    }

    /**
     * Adds a new row of data to the specified worksheet at the given index. The row is populated
     * with values from the class fields such as `team`, `played`, `runs`, `extras`, and others.
     *
     * @param worksheet The worksheet where the row should be added. Can be null.
     * @param index The index at which the row is to be created in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, team)
            createCellAndAddValue(2, played)
            createCellAndAddValue(3, runs)
            createCellAndAddValue(4, extras)
            createCellAndAddValue(5, byes)
            createCellAndAddValue(6, legByes)
            createCellAndAddValue(7, wides)
            createCellAndAddValue(8, noBalls)
            createCellAndAddValue(9, penalties)
            createCellAndAddValue(10, balls)
            createCellAndAddValue(11, wickets)
            createCellAndAddValue(12, percentage)
        }
    }
}