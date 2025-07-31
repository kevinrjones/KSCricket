package com.knowledgespike.feature.teamrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet

/**
 * Data Transfer Object representing the details of a team's performance target in a match.
 *
 * This class stores information regarding match-related details such as teams involved,
 * match and series dates, location, result description, and the target scores achieved.
 * It also provides functionality for serializing these details into CSV and Excel-compatible formats.
 *
 * Implements:
 *  - `IToCsv`: Enables representation of the data in CSV format.
 *  - `IToXls`: Provides methods to structure data for use in Excel worksheets.
 *
 * @property matchId Unique identifier for the match.
 * @property winningTeam The name of the team that won the match.
 * @property losingTeam The name of the team that lost the match.
 * @property matchTitle Title or description of the match.
 * @property seriesDate The date or period when the series took place.
 * @property matchDate The specific date on which the match occurred.
 * @property ground The venue or location where the match took place.
 * @property resultString A string representation of the match's result.
 * @property target The target score or points in the match.
 */
@Serializable
data class TeamTargetDetailsDto(
    val matchId: String,
    val winningTeam: String,
    val losingTeam: String,
    val matchTitle: String,
    val seriesDate: String,
    val matchDate: String,
    val ground: String,
    val resultString: String,
    val target: Int
) : IToCsv, IToXls {
    /**
     * Converts the details of the match into a CSV (Comma-Separated Values) formatted string.
     *
     * @param separator The string to use as the separator for the CSV fields.
     * @return A CSV formatted string containing match details, separated by the provided separator.
     */
    override fun toCsv(separator: String): String =
        "${winningTeam} $separator ${losingTeam} $separator ${matchTitle} $separator ${seriesDate} $separator ${matchDate} $separator ${ground} $separator ${resultString} $separator ${target}"
    

    /**
     * Generates a CSV header for the team target details.
     *
     * @param separator The string used to separate columns in the CSV header.
     * @return A string representing the header of the CSV for the team target details.
     */
    override fun csvHeader(separator: String): String =
        "Winning Team $separator Losing Team $separator Match Title $separator Series Date $separator Match Date $separator Location $separator Result String $separator Target"


    /**
     * Adds a header row to the given worksheet with predefined column titles.
     *
     * @param worksheet The `Sheet` instance where the header row will be added.
     *                   If null, no header is added.
     */
    override fun addHeader(worksheet: Sheet?) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, "Winning Team")
            createCellAndAddValue(2, "Losing Team")
            createCellAndAddValue(3, "Match Title")
            createCellAndAddValue(4, "Series Date")
            createCellAndAddValue(5, "Match Date")
            createCellAndAddValue(6, "Location")
            createCellAndAddValue(7, "Result")
            createCellAndAddValue(18, "Target")
        }
    }

    /**
     * Populates a row in the given worksheet at the specified index with values corresponding to
     * team match details such as winning team, losing team, match title, and other related information.
     *
     * @param worksheet The worksheet in which the row will be added. Can be null.
     * @param index The index of the row to be created and populated within the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {
        worksheet?.createRow(0)?.apply {
            createCellAndAddValue(1, winningTeam)
            createCellAndAddValue(2, losingTeam)
            createCellAndAddValue(3, matchTitle)
            createCellAndAddValue(4, seriesDate)
            createCellAndAddValue(5, matchDate)
            createCellAndAddValue(6, ground)
            createCellAndAddValue(7, resultString)
            createCellAndAddValue(18, target)
        }
    }
}
