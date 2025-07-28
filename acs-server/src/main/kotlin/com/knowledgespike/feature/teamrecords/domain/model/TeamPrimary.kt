package com.knowledgespike.feature.teamrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents the primary statistical data of a sports team within a series or match context.
 *
 * This data class is used to encapsulate various statistical metrics and metadata
 * associated with a team's performance, including matches played, runs scored, wickets lost,
 * and other performance indicators. Additionally, the data class provides functionality to
 * serialize this information into CSV and Excel-compatible formats.
 *
 * @property team Name of the team.
 * @property opponents Name of the opposing team(s).
 * @property played Number of matches played by the team.
 * @property won Number of matches won by the team.
 * @property drawn Number of matches that resulted in a draw.
 * @property lost Number of matches lost by the team.
 * @property tied Number of matches that ended in a tie.
 * @property innings Number of innings played by the team.
 * @property totalRuns Total runs scored by the team.
 * @property wickets Total number of wickets lost by the team.
 * @property totalBalls Total balls faced by the team.
 * @property hs Highest score achieved by the team.
 * @property ls Lowest score achieved by the team.
 * @property avg Average runs scored by the team.
 * @property rpo Runs per over statistic for the team.
 * @property sr Strike rate of the team.
 * @property bi Batting impact value of the team.
 * @property seriesDate Date of the series in which the performance occurred.
 * @property matchStartYear Starting year of the match or series.
 * @property ground Name of the ground where the match was played.
 * @property countryName Name of the country where the match/series occurred.
 */
@Serializable
data class TeamPrimary(
    val team: String,
    val opponents: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val tied: Int,
    val innings: Int,
    val totalRuns: Int,
    val wickets: Int,
    val totalBalls: Int,
    val hs: Int,
    val ls: Int,
    val avg: Double,
    val rpo: Double,
    val sr: Double,
    val seriesDate: String,
    val matchStartYear: String,
    val ground: String,
    val countryName: String
) : IToCsv, IToXls {
    /**
     * Converts the team's primary statistics into a CSV formatted string, with fields
     * separated by the specified delimiter.
     *
     * @param separator The character or string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the team's primary statistics.
     */
    override fun toCsv(separator: String): String {
        return "$team $separator $played $separator $opponents $separator $won $separator $drawn $separator $lost $separator $tied|$innings|$totalRuns $separator " +
                "$wickets $separator $avg $separator $rpo $separator $sr $separator $seriesDate $separator $matchStartYear $separator $ground $separator $countryName"
    }

    /**
     * Generates the CSV header row for team and match statistics.
     *
     * @param separator The delimiter to use between each column in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String =
        "Team $separator Played $separator Opponents $separator Won $separator Drawn $separator Lost $separator Tied|Innings|Total Runs $separator Wickets Lost $separator Average $separator " +
                "Runs Per Over $separator Strike Rate $separator Batting Impact $separator Series Date $separator Match Start Year $separator Ground $separator Country"

    /**
     * Populates the header row for the provided worksheet with predefined column titles
     * such as "Team," "Played," "Opponents," etc. Each cell in the header row corresponds
     * to specific team-related attributes.
     *
     * @param worksheet The sheet in which the header row will be created. If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {

        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Team")
            createCellAndAddValue(2, "Played")
            createCellAndAddValue(3, "Opponents")
            createCellAndAddValue(4, "Won")
            createCellAndAddValue(5, "Drawn")
            createCellAndAddValue(6, "Lost")
            createCellAndAddValue(7, "Tied")
            createCellAndAddValue(8, "Innings")
            createCellAndAddValue(9, "Total Runs")
            createCellAndAddValue(10, "Wickets Lost")
            createCellAndAddValue(11, "Average")
            createCellAndAddValue(12, "Runs Per Over")
            createCellAndAddValue(13, "Strike Rate")
            createCellAndAddValue(14, "Batting Impact")
            createCellAndAddValue(15, "Series Date")
            createCellAndAddValue(16, "Start Year")
            createCellAndAddValue(17, "Ground")
            createCellAndAddValue(18, "Country")
        }

    }

    /**
     * Adds a new line of data to the specified worksheet at the given index.
     *
     * @param worksheet The worksheet where the data row should be added.
     * @param index The index at which the row should be created in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {

        worksheet?.createRow(index)?.apply {

            createCellAndAddValue(1, team)
            createCellAndAddValue(2, played.toDouble())
            createCellAndAddValue(3, opponents)
            createCellAndAddValue(4, won.toDouble())
            createCellAndAddValue(5, drawn.toDouble())
            createCellAndAddValue(6, lost.toDouble())
            createCellAndAddValue(7, tied.toDouble())
            createCellAndAddValue(8, innings.toDouble())
            createCellAndAddValue(9, totalRuns.toDouble())
            createCellAndAddValue(10, wickets.toDouble())
            createCellAndAddValue(11, avg)
            createCellAndAddValue(12, rpo)
            createCellAndAddValue(13, sr)
            createCellAndAddValue(14, seriesDate)
            createCellAndAddValue(15, matchStartYear)
            createCellAndAddValue(16, ground)
            createCellAndAddValue(17, countryName)
        }
    }
}



