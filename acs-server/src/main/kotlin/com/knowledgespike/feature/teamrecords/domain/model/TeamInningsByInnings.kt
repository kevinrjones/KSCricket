package com.knowledgespike.feature.teamrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import com.knowledgespike.feature.teamrecords.data.repository.JooqTeamHelpers
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents statistical details of a team's performance in a single innings of a cricket match.
 *
 * This data class encapsulates various aspects of a team's innings such as match identifier,
 * team names, runs scored, overs bowled, result, location, and other match-specific details.
 * It provides functionality to serialize its data to a CSV format or populate Excel spreadsheets.
 *
 * @property matchId A unique identifier for the match.
 * @property team The name of the team whose innings is represented.
 * @property opponents The name of the opposing team.
 * @property innings The number of the innings in the match (e.g., 1 or 2).
 * @property totalRuns The total runs scored by the team in this innings.
 * @property rpo The run rate per over (runs scored per over) for the innings.
 * @property resultString A string describing the result related to the team.
 * @property ground The location where the match was played.
 * @property matchDate The date when the match was played.
 * @property totalWickets The total wickets lost by the team in this innings.
 * @property ballsBowled The total number of balls bowled by the team in this innings.
 * @property ballsPerOver The standard number of balls per over (e.g., 6 in most formats).
 */
@Serializable
data class TeamInningsByInnings(
    val matchId: String,
    val caId: String,
    val team: String,
    val opponents: String,
    val innings: Int,
    val totalRuns: Int,
    val rpo: Double,
    val resultString: String,
    val ground: String,
    val matchDate: String,
    val totalWickets: Int,
    val ballsBowled: Int,
    val ballsPerOver: Int,
    val allOut: Boolean,
    val declared: Boolean,
) : IToCsv, IToXls {
    /**
     * Converts the team's innings data into a CSV formatted string, with fields separated by the specified delimiter.
     *
     * @param separator The character or string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the team's innings data, including team details,
     *         match statistics, and performance metrics.
     */
    override fun toCsv(separator: String): String {
        val overs = JooqTeamHelpers.getOvers(ballsBowled, ballsPerOver)
        return "$team $separator $opponents $separator " +
                "$innings $separator $totalRuns $separator $overs $separator $rpo $separator $resultString $separator" +
                "$ground $separator $matchDate $separator $totalWickets $separator $allOut $separator $declared"
    }

    /**
     * Generates a CSV header string for team innings statistics.
     *
     * @param separator The delimiter to use between the column names in the header.
     * @return A string representing the CSV header with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Team $separator Opponents $separator Innings $separator Score $separator Overs $separator Runs Per Over $separator Result $separator Ground $separator Start Date $separator Wickets $separator AllOut $separator Declared"
    }

    /**
     * Adds a header row to the specified worksheet with predefined column titles
     * representing attributes related to team innings data.
     *
     * @param worksheet The worksheet where the header row will be created.
     *                  If the worksheet is null, no action will be performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Team")
            createCellAndAddValue(2, "Opponents")
            createCellAndAddValue(3, "Innings")
            createCellAndAddValue(4, "Score")
            createCellAndAddValue(5, "Overs")
            createCellAndAddValue(6, "Runs Per Over")
            createCellAndAddValue(7, "Result")
            createCellAndAddValue(8, "Ground")
            createCellAndAddValue(9, "Start Date")
            createCellAndAddValue(10, "Wickets")
            createCellAndAddValue(11, "All Out")
            createCellAndAddValue(12, "Declared")

        }
    }

    /**
     * Adds a line of data to the specified worksheet at the given row index.
     *
     * This method creates a new row in the provided worksheet and populates it with
     * data related to a team's innings, including team name, opponent, score, overs,
     * run rate, match result, location, and match date.
     *
     * @param worksheet The `Sheet` object representing the worksheet to which the row should be added.
     *                  If null, the function will not perform any operations.
     * @param index The index of the row to be created in the spreadsheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {

        val overs = JooqTeamHelpers.getOvers(ballsBowled, ballsPerOver)

        worksheet?.createRow(index)?.apply {
            createCellAndAddValue(1, team)
            createCellAndAddValue(2, opponents)
            createCellAndAddValue(3, innings)
            createCellAndAddValue(4, totalRuns)
            createCellAndAddValue(5, overs)
            createCellAndAddValue(6, rpo)
            createCellAndAddValue(7, resultString)
            createCellAndAddValue(8, ground)
            createCellAndAddValue(9, matchDate)
            createCellAndAddValue(10, totalWickets)
            createCellAndAddValue(11, allOut)
            createCellAndAddValue(12, declared)
        }
    }
}
