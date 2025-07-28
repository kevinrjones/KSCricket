package com.knowledgespike.feature.battingrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet
import kotlin.math.floor

/**
 * Represents the primary batting statistics for a cricket player.
 *
 * This class contains detailed information about a player's batting performance,
 * including overall runs, average, strike rate, and more, against specific teams
 * and in specific conditions such as years and grounds.
 *
 * Implements:
 * - `IToCsv`: To provide functionality for CSV serialization including data and header rows.
 * - `IToXls`: To enable Excel-compatible data serialization supporting headers and data rows.
 *
 * Properties include player identification details, performance metrics such as
 * runs, averages, highest scores, and contextual details like the team and country.
 */
@Serializable
data class BattingPrimary(
    val playerId: Int,
    val name: String,
    val sortNamePart: String,
    val debut: String,
    val end: String,
    val team: String,
    val opponents: String,
    val matches: Int,
    val innings: Int,
    val notOuts: Int,
    val runs: Int,
    val highestScore: Double,
    val avg: Double,
    val sr: Double,
    val bi: Double,
    val hundreds: Int,
    val fifties: Int,
    val ducks: Int,
    val fours: Int,
    val sixes: Int,
    val balls: Int,
    val year: String,
    val ground: String,
    val countryName: String,
) : IToCsv, IToXls {
    /**
     * Converts the batting data to a CSV formatted string using the specified separator.
     *
     * @param separator The delimiter used to separate values in the resulting CSV string.
     * @return A CSV-formatted string representation of the batting data using the specified separator.
     */
    override fun toCsv(separator: String): String {
        val minHS = floor(highestScore)
        val notOut = minHS < highestScore

        return "$name $separator $team $separator $opponents $separator $year $separator " +
                "$matches $separator $innings $separator $runs $separator $notOuts $separator " +
                "$minHS $separator $notOut $separator $avg $separator $hundreds $separator " +
                "$fifties $separator $ducks $separator " +
                "$sixes $separator $fours $separator $balls $separator " +
                "$ground $separator $countryName"

    }

    /**
     * Generates the CSV header row for a dataset containing batting statistics.
     *
     * @param separator The delimiter to be used between column headers in the CSV line.
     * @return A string representing the CSV header row, with column titles separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String =
        "Name $separator Team $separator Opponents $separator Year $separator " +
                "Matches $separator Innings $separator Runs $separator NotOuts $separator " +
                "Highest Score $separator Not Out $separator Average $separator Hundreds $separator " +
                "Fifties $separator Ducks $separator Sixes $separator Fours $separator " +
                "Balls $separator Ground $separator CountryName"

    /**
     * Adds a header row to the specified worksheet with predefined column titles related to batting statistics.
     *
     * @param worksheet The sheet to which the header row will be added. If null, the method does nothing.
     */
    override fun addHeader(worksheet: Sheet?) {

        val row = worksheet?.createRow(0)
        row?.createCellAndAddValue(1,"Name")
        row?.createCellAndAddValue(2,"SortNamePart")
        row?.createCellAndAddValue(3,"Team")
        row?.createCellAndAddValue(4,"Opponents")
        row?.createCellAndAddValue(5,"Year")
        row?.createCellAndAddValue(6,"Matches")
        row?.createCellAndAddValue(7,"Innings")
        row?.createCellAndAddValue(8,"Runs")
        row?.createCellAndAddValue(9,"Not Outs")
        row?.createCellAndAddValue(10,"Highest Score")
        row?.createCellAndAddValue(11,"Not Out")
        row?.createCellAndAddValue(12,"Average")
        row?.createCellAndAddValue(13,"Hundreds")
        row?.createCellAndAddValue(14,"Fifties")
        row?.createCellAndAddValue(15,"Ducks")
        row?.createCellAndAddValue(16,"Sixes")
        row?.createCellAndAddValue(17,"Fours")
        row?.createCellAndAddValue(18,"Balls")
        row?.createCellAndAddValue(19,"Ground")
        row?.createCellAndAddValue(20,"Country Name")

    }

    /**
     * Adds a row of batting statistics to the specified worksheet at the given row index.
     *
     * @param worksheet The worksheet to which the data row should be added. Can be null.
     * @param index The row index in the worksheet where the data should be inserted.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {

        val minHS = floor(highestScore)
        val notOut = minHS < highestScore

        val row = worksheet?.createRow(index)

        row?.createCellAndAddValue(1,name)
        row?.createCellAndAddValue(2,sortNamePart)
        row?.createCellAndAddValue(3,team)
        row?.createCellAndAddValue(4,opponents)
        row?.createCellAndAddValue(5,year)
        row?.createCellAndAddValue(6,matches)
        row?.createCellAndAddValue(7,innings)
        row?.createCellAndAddValue(8,runs)
        row?.createCellAndAddValue(9,notOuts)
        row?.createCellAndAddValue(10,minHS)
        row?.createCellAndAddValue(11,notOut)
        row?.createCellAndAddValue(12,avg)
        row?.createCellAndAddValue(13,hundreds)
        row?.createCellAndAddValue(14,fifties)
        row?.createCellAndAddValue(15,ducks)
        row?.createCellAndAddValue(16,sixes)
        row?.createCellAndAddValue(17,fours)
        row?.createCellAndAddValue(18,balls)
        row?.createCellAndAddValue(19,ground)
        row?.createCellAndAddValue(20,countryName)
    }
}



