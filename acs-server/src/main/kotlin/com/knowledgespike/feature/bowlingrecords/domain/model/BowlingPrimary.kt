package com.knowledgespike.feature.bowlingrecords.domain.model

import com.knowledgespike.core.contenttypeconverters.IToCsv
import com.knowledgespike.core.contenttypeconverters.IToXls
import com.knowledgespike.core.serialization.createCellAndAddValue
import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.Sheet


/**
 * Represents the primary bowling statistics of a cricket player.
 *
 * This data class holds comprehensive statistical information about
 * a player’s bowling performance, including matches played, innings,
 * balls bowled, runs conceded, and wickets taken. Additionally, it
 * encapsulates details such as bowling averages, strike rates, and
 * significant achievements like five-wicket or ten-wicket hauls.
 *
 * Implements both CSV and Excel-compatible serialization interfaces,
 * allowing its data to be serialized into these formats.
 *
 * @property playerId The unique identifier of the player.
 * @property name The full name of the player.
 * @property sortNamePart A part of the name used for sorting purposes.
 * @property team The name of the team the player belongs to.
 * @property opponents The name of the opposing team, if available.
 * @property matches The total number of matches played by the player.
 * @property innings The number of innings bowled by the player.
 * @property balls The total number of balls delivered by the player.
 * @property maidens The number of maiden overs bowled by the player.
 * @property dots The number of dot balls delivered by the player.
 * @property runs The total number of runs conceded by the player.
 * @property wickets The total number of wickets taken by the player.
 * @property ground The ground where the relevant performance occurred.
 * @property countryName The name of the country associated with the performance.
 * @property year The year in which the performance was recorded.
 * @property avg The bowling average of the player.
 * @property sr The bowling strike rate of the player.
 * @property bi The bowling impact, representing an aggregate performance metric.
 * @property fours The total number of fours conceded by the player.
 * @property sixes The total number of sixes conceded by the player.
 * @property fiveFor The number of five-wicket hauls taken by the player.
 * @property tenFor The number of ten-wicket hauls taken by the player.
 * @property bbi The best bowling performance in an innings, stored in decimal format.
 * @property bbm The best bowling performance in a match, stored in decimal format.
 */
@Serializable
data class BowlingPrimary(
    val playerId: Int,
    val name: String,
    val sortNamePart: String,
    val debut: String,
    val end: String,
    val team: String,
    val opponents: String?,
    val matches: Int,
    val innings: Int?,
    val balls: Int?,
    val maidens: Int?,
    val dots: Int?,
    val runs: Int?,
    val wickets: Int,
    val ground: String,
    val countryName: String,
    val year: String,
    val avg: Double?,
    val sr: Double?,
    val bi: Double?,
    val fours: Int?,
    val sixes: Int?,
    val fiveFor: Int?,
    val tenFor: Int?,
    val bbi: Double?,
    val bbm: Double?
) : IToCsv, IToXls {
    /**
     * Converts the bowling statistics of a player into a CSV formatted string, with fields
     * separated by the specified delimiter.
     *
     * @param separator The character or string used to separate values in the resulting CSV.
     * @return A CSV-formatted string representing the player's bowling statistics.
     */
    override fun toCsv(separator: String): String {
        val (bbiw, bbir) = getIndividualBb(bbi)
        val (bbmw, bbmr) = getIndividualBb(bbm)

        return "$name $separator  $sortNamePart $separator  $team $separator  $opponents $separator  $year $separator  $matches $separator  " +
                "$innings $separator  $balls $separator  $maidens $separator  $dots $separator  $runs $separator  $wickets $separator  " +
                "$avg $separator  $sr $separator  $bi $separator  $fours $separator  $sixes $separator  $fiveFor $separator  $tenFor $separator  $bbiw $separator  $bbir $separator  " +
                "$bbmw $separator $bbmr $separator $ground $separator $countryName"
    }

    /**
     * Generates the CSV header row for bowling statistics.
     *
     * @param separator The delimiter to use between each column in the CSV header.
     * @return A string representing the CSV header row with column names separated by the specified delimiter.
     */
    override fun csvHeader(separator: String): String =

        "Name $separator  SortNamePart $separator  Team $separator  Opponents $separator  Year $separator  Matches $separator  " +
                "Innings $separator  Balls $separator  Maidens $separator  Dots $separator  Runs $separator  Wickets $separator  " +
                "Avg $separator  SR $separator  Batting Impact $separator  Fours $separator  Sixes $separator  FiveFor $separator  TenFor $separator  BBI - Wickets $separator  BBI - Runs" +
                " $separator  BBM Wickets $separator  BBM Runs $separator Ground $separator Country"

    /**
     * Calculates and returns the individual components of a "best bowling in an innings" value (BBI).
     * The method separates the input value into two components: the integer part
     * (representing the number of wickets) and the fractional part (representing the runs conceded).
     *
     * @param bbiValue The BBI value, where the integer part represents the number of wickets
     *                 and the fractional part represents the runs conceded. It can be null.
     * @return A pair of strings where the first element is the integer part (number of wickets)
     *         and the second element is the fractional part (runs conceded) of the BBI value.
     */
    private fun getIndividualBb(bbiValue: Double?): Pair<String, String> {
        var bbiw = ""
        var bbir = ""
        if (bbiValue != null) {
            // ReSharper disable once InconsistentNaming
            val bbiw_d = Math.floor(bbiValue)
            // ReSharper disable once InconsistentNaming
            val bbir_d = bbiw_d - bbiValue
            bbiw = bbiw_d.toString()
            bbir = bbir_d.toString()
        }
        return Pair(bbiw, bbir)
    }

    /**
     * Adds a header row to the specified worksheet with predefined column names related to bowling statistics.
     *
     * @param worksheet An instance of the `Sheet` where the header row will be added. If the worksheet is null, no action will be taken.
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
        row?.createCellAndAddValue(8,"Balls")
        row?.createCellAndAddValue(9,"Maidens")
        row?.createCellAndAddValue(10,"Dots")
        row?.createCellAndAddValue(11,"Runs")
        row?.createCellAndAddValue(12,"Wickets")
        row?.createCellAndAddValue(13,"Average")
        row?.createCellAndAddValue(14,"Strike Rate")
        row?.createCellAndAddValue(15,"Bowling Impact")
        row?.createCellAndAddValue(16,"Fours")
        row?.createCellAndAddValue(17,"Sixes")
        row?.createCellAndAddValue(18,"Five For")
        row?.createCellAndAddValue(19,"Ten For")
        row?.createCellAndAddValue(20,"BBI Wickets")
        row?.createCellAndAddValue(21,"BBI Runs")
        row?.createCellAndAddValue(22,"BBM Wickets")
        row?.createCellAndAddValue(23,"BBM Runs")
        row?.createCellAndAddValue(24,"Ground")
        row?.createCellAndAddValue(25,"Country")

    }

    /**
     * Adds a line of data to a specified worksheet at the given row index.
     * This method populates the row with various bowling statistics such as matches, innings, runs, wickets, and more.
     *
     * @param worksheet The worksheet where the data will be added. Can be null.
     * @param index The index of the row to be created and populated in the worksheet.
     */
    override fun addLine(worksheet: Sheet?, index: Int) {

        val (bbiw, bbir) = getIndividualBb(bbi)
        val (bbmw, bbmr) = getIndividualBb(bbm)

        val row = worksheet?.createRow(index)

        // todo: See InningsByInningsBowling, need to handle the null cases correctly
        // todo: change this so that bbm and bbi are output correctly, ie need 10/43 type columns - csv output probably also needs to be corrected
        row?.createCellAndAddValue(1,name)
        row?.createCellAndAddValue(2,sortNamePart)
        row?.createCellAndAddValue(3,team)
        row?.createCellAndAddValue(4,opponents ?: "")
        row?.createCellAndAddValue(5,year)
        row?.createCellAndAddValue(6,matches.toDouble())
        row?.createCellAndAddValue(7,innings?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(8,balls?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(9,maidens?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(10,dots?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(11,runs?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(12,wickets.toDouble())
        row?.createCellAndAddValue(13,avg ?: 0.0)
        row?.createCellAndAddValue(14,sr ?: 0.0)
        row?.createCellAndAddValue(15,bi ?: 0.0)
        row?.createCellAndAddValue(16,fours?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(17,sixes?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(18,fiveFor?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(19,tenFor?.toDouble() ?: 0.0)
        row?.createCellAndAddValue(20,bbiw)
        row?.createCellAndAddValue(21,bbir)
        row?.createCellAndAddValue(22,bbmw)
        row?.createCellAndAddValue(23,bbmr)
        row?.createCellAndAddValue(24,ground)
        row?.createCellAndAddValue(25,countryName)
    }
}



