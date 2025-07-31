package com.knowledgespike.feature.teamrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents team-specific details and statistics related to the extras conceded during cricket match innings.
 *
 * This data class captures various metrics and contextual information for a particular match,
 * such as the team's identity, opponents, types of extras (like wides, no balls, leg byes, etc.),
 * and match context. It implements CSV and Excel export functionalities for reporting purposes.
 *
 * @property matchId Unique identifier of the match.
 * @property team Name of the team for which the stats are recorded.
 * @property extras Total number of extras conceded by the team.
 * @property byes Number of byes conceded by the team.
 * @property legByes Number of leg byes conceded by the team.
 * @property wides Number of wides conceded by the team.
 * @property noBalls Number of no-balls conceded by the team.
 * @property penalties Number of penalty runs conceded by the team.
 * @property total Total extras conceded in the innings.
 * @property wickets Total number of wickets lost by the team at the time of evaluation.
 * @property overs Total number of overs bowled during the match/innings.
 * @property ground Venue where the match was played.
 * @property opponents Name of the opposing team.
 * @property matchStartDate Start date of the match.
 * @property percentage Percentage representation for specific evaluations (if applicable).
 */
@Serializable
data class TeamExtrasInningsByInnings(
    val matchId: String,
    val team: String,
    val extras: Int,
    val byes: Int,
    val legByes: Int,
    val wides: Int,
    val noBalls: Int,
    val penalties: Int,
    val total: Int,
    val wickets: Int,
    val overs: String,
    val ground: String,
    val opponents: String,
    val matchStartDate: String,
    val percentage: Double?
) : IToCsv, IToXls {

    /**
     * Generates the CSV header row for the team extras statistics.
     *
     * @param separator The delimiter to use between each column in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Team $separator Opponents $separator Extras $separator Byes $separator Leg Byes $separator Wides $separator No Balls $separator Penalties $separator Total $separator Wickets $separator Overs $separator Ground $separator Date $separator Percentage"
    }

    /**
     * Converts the current object's properties into a CSV-formatted string, using the specified separator.
     *
     * @param separator The delimiter used to separate the properties in the resulting CSV string.
     * @return A CSV-formatted string representation of the object's properties.
     */
    override fun toCsv(separator: String): String {
        return "$team $separator $opponents $separator $extras $separator $byes $separator $legByes $separator $wides $separator $noBalls $separator $penalties $separator $total $separator $wickets $separator $overs $separator $ground $separator $matchStartDate $separator $percentage"
    }

    /**
     * Populates the header row for the provided worksheet with predefined column titles such as "Team," "Opponents,"
     * "Extras," etc. Each cell in the header row corresponds to a specific entity related to team and match metrics.
     *
     * @param worksheet The sheet in which the header row will be created. If null, no action is performed.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Team")
            createCellAndAddValue(2, "Opponents")
            createCellAndAddValue(3, "Extras")
            createCellAndAddValue(4, "Byes")
            createCellAndAddValue(5, "Leg Byes")
            createCellAndAddValue(6, "Wides")
            createCellAndAddValue(7, "No Balls")
            createCellAndAddValue(8, "Penalties")
            createCellAndAddValue(9, "Total")
            createCellAndAddValue(10, "Wickets")
            createCellAndAddValue(11, "Overs")
            createCellAndAddValue(12, "Ground")
            createCellAndAddValue(13, "Match Start Date")
            createCellAndAddValue(14, "Percentage")
        }
    }

    /**
     * Adds a line to the specified worksheet at a given index. This method populates a row with pre-defined data
     * such as team information, match details, and statistical values.
     *
     * @param worksheet The sheet where the row will be added. If null, no operation is performed.
     * @param index The index where the row is to be created in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, team)
            createCellAndAddValue(2, opponents)
            createCellAndAddValue(3, extras)
            createCellAndAddValue(4, byes)
            createCellAndAddValue(5, legByes)
            createCellAndAddValue(6, wides)
            createCellAndAddValue(7, noBalls)
            createCellAndAddValue(8, penalties)
            createCellAndAddValue(9, total)
            createCellAndAddValue(10, wickets)
            createCellAndAddValue(11, overs)
            createCellAndAddValue(12, ground)
            createCellAndAddValue(13, matchStartDate)
            if (percentage == null)
                createCellAndAddValue(13, "-")
            else
                createCellAndAddValue(8, percentage)
        }
    }
}