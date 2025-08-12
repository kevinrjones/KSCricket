package com.ksstats.feature.playerbattingprimarystats.data

import com.knowledgespike.feature.battingrecords.domain.model.BattingPrimary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class PrimaryBattingTest {

    @Test
    fun `ToCsv should return correctly formatted CSV string`() {
        val primaryBatting = BattingPrimary(
            playerId = 1,
            name = "Player1",
            team = "TeamA",
            opponents = "TeamB",
            sortNamePart = "P",
            matches = 10,
            innings = 8,
            notOuts = 2,
            runs = 500,
            highestScore = 120.5,
            avg = 62.5,
            sr = 140.0,
            bi = 85.0,
            hundreds = 2,
            fifties = 3,
            ducks = 0,
            fours = 40,
            sixes = 25,
            balls = 300,
            year = "2023",
            ground = "Stadium1",
            countryName = "Country1",
            debut = "",
            end = "",
        )

        val expectedCsv =
            "Player1,TeamA,TeamB,2023,10,8,500,2,120.0,true,62.5,2,3,0,25,40,300,Stadium1,Country1"
        val actualCsv = primaryBatting.toCsv(" , ")

        assertEquals(expectedCsv.replace(Regex("\\s+"), ""), actualCsv.replace(Regex("\\s+"), ""))
    }

    @Test
    fun `CsvHeader should return correct CSV header`() {
        val primaryBatting = BattingPrimary(
            playerId = 1,
            name = "Player1",
            team = "TeamA",
            opponents = "TeamB",
            sortNamePart = "P",
            matches = 10,
            innings = 8,
            notOuts = 2,
            runs = 500,
            highestScore = 120.5,
            avg = 62.5,
            sr = 140.0,
            bi = 85.0,
            hundreds = 2,
            fifties = 3,
            ducks = 0,
            fours = 40,
            sixes = 25,
            balls = 300,
            year = "2023",
            ground = "Stadium1",
            countryName = "Country1",
            debut = "",
            end = "",
        )

        val expectedHeader = """Name , Team , Opponents , Year , 
Matches , Innings , Runs , NotOuts , 
Highest Score , Not Out , Average , Hundreds , 
Fifties , Ducks , 
Sixes , Fours , Balls , 
Ground , CountryName"""

        val actualHeader = primaryBatting.csvHeader(" , ")

        assertEquals(expectedHeader.replace(Regex("\\s+"), " "), actualHeader.replace(Regex("\\s+"), " "))
    }

    @Test
    fun `AddHeader should correctly add header to worksheet`() {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("TestSheet")
        val primaryBatting = BattingPrimary(
            playerId = 1,
            name = "Player1",
            team = "TeamA",
            opponents = "TeamB",
            sortNamePart = "P",
            matches = 10,
            innings = 8,
            notOuts = 2,
            runs = 500,
            highestScore = 120.5,
            avg = 62.5,
            sr = 140.0,
            bi = 85.0,
            hundreds = 2,
            fifties = 3,
            ducks = 0,
            fours = 40,
            sixes = 25,
            balls = 300,
            year = "2023",
            ground = "Stadium1",
            countryName = "Country1",
            debut = "",
            end = "",
        )

        primaryBatting.addHeader(sheet)

        val headerRow = sheet.getRow(0)
        assertNotNull(headerRow)
        assertEquals("Name", headerRow.getCell(1).stringCellValue)
        assertEquals("SortNamePart", headerRow.getCell(2).stringCellValue)
        assertEquals("Team", headerRow.getCell(3).stringCellValue)
        assertEquals("Opponents", headerRow.getCell(4).stringCellValue)
    }

    @Test
    fun `AddLine should correctly add data to worksheet`() {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("TestSheet")
        val primaryBatting = BattingPrimary(
            playerId = 1,
            name = "Player1",
            team = "TeamA",
            opponents = "TeamB",
            sortNamePart = "P",
            matches = 10,
            innings = 8,
            notOuts = 2,
            runs = 500,
            highestScore = 120.5,
            avg = 62.5,
            sr = 140.0,
            bi = 85.0,
            hundreds = 2,
            fifties = 3,
            ducks = 0,
            fours = 40,
            sixes = 25,
            balls = 300,
            year = "2023",
            ground = "Stadium1",
            countryName = "Country1",
            debut = "",
            end = "",
        )

        primaryBatting.addLine(sheet, 1)

        val dataRow = sheet.getRow(1)
        assertNotNull(dataRow)
        assertEquals("Player1", dataRow.getCell(1).stringCellValue)
        assertEquals("P", dataRow.getCell(2).stringCellValue)
        assertEquals("TeamA", dataRow.getCell(3).stringCellValue)
        assertEquals("TeamB", dataRow.getCell(4).stringCellValue)
        assertTrue(dataRow.getCell(10).numericCellValue < 121)
    }
}