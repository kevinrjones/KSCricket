package com.knowledgespike.feature.teamrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import com.knowledgespike.feature.scorecard.domain.model.VictoryType
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Represents the result of a team match, including details about the match identifier, teams, outcome,
 * and additional match metadata. This data class provides methods for exporting match results to
 * CSV and Excel-compatible formats.
 *
 * Implements:
 * - [IToCsv]: for serializing match results into CSV format.
 * - [IToXls]: for populating Excel sheets with match data.
 *
 * Properties:
 * - `matchId`: Identifier for the specific match.
 * - `team`: Name of the team participating in the match.
 * - `opponents`: Name of the opponent team.
 * - `victoryType`: Enum indicating the type of victory.
 * - `howMuch`: Numerical value describing the margin of victory or significant statistic.
 * - `location`: Location where the match took place.
 * - `matchStartDate`: Start date of the match in string format.
 * - `resultString`: Summary of the match result as a string.
 * - `teamId`: Identifier for the team.
 * - `opponentsId`: Identifier for the opponent team.
 * - `whoWonId`: Identifier for the team that won the match.
 * - `teamTossId`: Identifier for the team that won the toss.
 */
@Serializable
data class TeamMatchResult(
    val matchId: String,
    val team: String,
    val opponents: String,
    val victoryType: VictoryType,
    val howMuch: Int,
    val ground: String,
    val matchStartDate: String,
    val resultString: String,
    val teamId: Int,
    val opponentsId: Int,
    val whoWonId: Int,
    val teamTossId: Int
) : IToCsv, IToXls {
    /**
     * Converts the match result data into a CSV-formatted string using the specified separator.
     *
     * @param separator The character or string used to separate values in the resulting CSV string.
     * @return A CSV-formatted string representing the match result data.
     */
    override fun toCsv(separator: String): String {
        return "$team $separator $opponents $separator " +
                "$victoryType $separator $howMuch $separator $ground $separator $matchStartDate $separator $resultString"
    }

    /**
     * Generates a CSV header string for team match result data.
     *
     * @param separator The delimiter to use between each column in the CSV header.
     * @return A CSV header row as a string, with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String {
        return "Team $separator Opponents $separator VictoryType $separator How Much $separator Location" +
                " $separator Start Date $separator Result"

    }

    /**
     * Adds a header row to the provided worksheet with predefined column titles.
     *
     * @param worksheet The worksheet where the header row will be added. If null, no header will be added.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Team")
            createCellAndAddValue(2, "Opponents")
            createCellAndAddValue(3, "Victory Type")
            createCellAndAddValue(4, "How Much")
            createCellAndAddValue(5, "Location")
            createCellAndAddValue(6, "Match Date")
            createCellAndAddValue(7, "Result")
        }
    }

    /**
     * Adds a new line to the given worksheet at the specified index with details of a team match result.
     *
     * @param worksheet The worksheet to which the line will be added. If null, no operation is performed.
     * @param index The index of the row in the worksheet where the line should be added.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(index)?.apply {
            createCellAndAddValue(1, team)
            createCellAndAddValue(2, opponents)
            createCellAndAddValue(3, victoryType.name)
            createCellAndAddValue(4, howMuch)
            createCellAndAddValue(5, ground)
            createCellAndAddValue(6, matchStartDate)
            createCellAndAddValue(7, resultString)
        }
    }
}
