package com.knowledgespike.feature.scorecard.data.repository

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.knowledgespike.DIALECT
import com.knowledgespike.core.jooq.getValueOrNull
import com.knowledgespike.core.type.error.Error
import com.knowledgespike.core.type.error.FindError
import com.knowledgespike.core.type.values.CaId
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.scorecard.domain.model.*
import com.knowledgespike.feature.scorecard.domain.repository.ScorecardRepository
import com.knowledgespike.plugins.DataSource
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.using


/**
 * Repository implementation for accessing and processing cricket scorecard data using jOOQ.
 * Provides methods to fetch and construct comprehensive scorecard details from the database.
 *
 * This class interacts with the underlying data source to retrieve match-related information,
 * including innings details, fall of wickets, officials, player performances, and match notes.
 *
 * @property dataSource The database connection source used for jOOQ DSL context creation.
 */
class JooqScorecardRepository(private val dataSource: DataSource) : ScorecardRepository {
    /**
     * Retrieves the scorecard for a given match based on the provided match identifier.
     *
     * @param caId The unique identifier for the match.
     * @return Either an Error in case of failure or a ScorecardDto containing the scorecard details if successful.
     */
    override fun getScorecard(caId: CaId): Either<Error, ScorecardDto> {

        val context = using(dataSource.dataSource, DIALECT)
        var matchId = 0

        var innings = listOf<LocalInningsDto>()
        var fallOfWicket = listOf<LocalFowDto>()
        var umpires = listOf<PersonDto>()
        var matchReferees = listOf<PersonDto>()
        var tvUmpires = listOf<PersonDto>()
        var scorers = listOf<PersonDto>()
        var playersOfTheMatch = listOf<PersonDto>()
        var closeOfPlay = listOf<CloseOfPlayDto>()
        var debuts = listOf<DebutDto>()
        var batting = listOf<LocalBattingDetailDto>()
        var bowling = listOf<LocalBowlingDetailDto>()
        var partnerships = listOf<LocalPartnershipDetails>()
        var matchData: LocalMatchDto? = null

        val res = findMatchWithId(context, caId)
            .flatMap { matchDto ->
                matchData = matchDto
                matchId = matchDto.matchId
                getInnings(context, matchDto.matchId)

            }
            .flatMap { inn ->
                innings = inn
                getFallOfWickets(context, matchId)
            }
            .flatMap { fow ->
                fallOfWicket = fow
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "UmpiresMatches",
                    officialsTable = "Umpires"
                )
            }
            .flatMap { ump ->
                umpires = ump
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "MatchRefereesMatches",
                    officialsTable = "MatchReferees"
                )
            }
            .flatMap { refs ->
                matchReferees = refs
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "TvUmpiresMatches",
                    officialsTable = "TvUmpires"
                )
            }
            .flatMap { tvUmps ->
                tvUmpires = tvUmps
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "ScorersMatches",
                    officialsTable = "Scorers"
                )
            }
            .flatMap { scrs ->
                scorers = scrs
                getPlayerOfTheMatch(context, matchId)
            }
            .flatMap { players ->
                playersOfTheMatch = players
                getCloseOfPlay(context, matchId)
            }
            .flatMap { cop ->
                closeOfPlay = cop
                getDebuts(context, matchId, matchData!!.matchType)
            }
            .flatMap { dbts ->
                debuts = dbts
                getBatting(context, matchId)
            }
            .flatMap { bat ->
                batting = bat
                getBowling(context, matchId)
            }
            .flatMap { bowl ->
                bowling = bowl
                getPartnerships(context, matchId)
            }
            .map { parts -> partnerships = parts }

        // return if error
        res.mapLeft { return it.left() }


        val scorecardDto = createScoreCard(
            matchData!!,
            innings,
            fallOfWicket,
            umpires,
            matchReferees,
            tvUmpires,
            scorers,
            playersOfTheMatch,
            closeOfPlay,
            debuts,
            batting,
            bowling,
            partnerships
        )

        return scorecardDto.right()
    }

    /**
     * Retrieves the scorecard for a specific cricket match based on the provided home team name, away team name, and date.
     *
     * @param homeTeamName the name of the home team in the match
     * @param awayTeamName the name of the away team in the match
     * @param date the date of the match in YYYY-MM-DD format
     * @return an instance of Either<Error, ScorecardDto>, where the left side represents an error and the right side contains the scorecard data
     */
    override fun getScorecard(homeTeamName: String, awayTeamName: String, date: String): Either<Error, ScorecardDto> {
        val context = using(dataSource.dataSource, DIALECT)
        var matchId = 0

        var innings = listOf<LocalInningsDto>()
        var fallOfWicket = listOf<LocalFowDto>()
        var umpires = listOf<PersonDto>()
        var matchReferees = listOf<PersonDto>()
        var tvUmpires = listOf<PersonDto>()
        var scorers = listOf<PersonDto>()
        var playersOfTheMatch = listOf<PersonDto>()
        var closeOfPlay = listOf<CloseOfPlayDto>()
        var notes = listOf<String>()
        var debuts = listOf<DebutDto>()
        var batting = listOf<LocalBattingDetailDto>()
        var bowling = listOf<LocalBowlingDetailDto>()
        var partnerships = listOf<LocalPartnershipDetails>()
        var matchData: LocalMatchDto? = null

        val res = findMatch(context, homeTeamName, awayTeamName, date)
            .flatMap { matchDto ->
                matchData = matchDto
                matchId = matchDto.matchId
                getInnings(context, matchDto.matchId)

            }
            .flatMap { inn ->
                innings = inn
                getFallOfWickets(context, matchId)
            }
            .flatMap { fow ->
                fallOfWicket = fow
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "UmpiresMatches",
                    officialsTable = "Umpires"
                )
            }
            .flatMap { ump ->
                umpires = ump
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "MatchRefereesMatches",
                    officialsTable = "MatchReferees"
                )
            }
            .flatMap { refs ->
                matchReferees = refs
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "TvUmpiresMatches",
                    officialsTable = "TvUmpires"
                )
            }
            .flatMap { tvUmps ->
                tvUmpires = tvUmps
                getOfficials(
                    context,
                    matchId,
                    matchesTable = "ScorersMatches",
                    officialsTable = "Scorers"
                )
            }
            .flatMap { scrs ->
                scorers = scrs
                getPlayerOfTheMatch(context, matchId)
            }
            .flatMap { players ->
                playersOfTheMatch = players
                getCloseOfPlay(context, matchId)
            }
            .flatMap { cop ->
                closeOfPlay = cop
                getNotes(context, matchId)
            }
            .flatMap { ns ->
                notes = ns
                getDebuts(context, matchId, matchData!!.matchType)
            }
            .flatMap { dbts ->
                debuts = dbts
                getBatting(context, matchId)
            }
            .flatMap { bat ->
                batting = bat
                getBowling(context, matchId)
            }
            .flatMap { bowl ->
                bowling = bowl
                getPartnerships(context, matchId)
            }
            .map { parts -> partnerships = parts }

        // return if error
        res.mapLeft { return it.left() }


        val scorecardDto = createScoreCard(
            matchData!!,
            innings,
            fallOfWicket,
            umpires,
            matchReferees,
            tvUmpires,
            scorers,
            playersOfTheMatch,
            closeOfPlay,
            debuts,
            batting,
            bowling,
            partnerships
        )

        return scorecardDto.right()
    }

    /**
     * Creates a comprehensive scorecard for a cricket match by aggregating various match-related details.
     *
     * @param matchData Contains the core details of the match such as teams, location, and result.
     * @param innings A list of innings data representing the performance of teams in each innings.
     * @param fallOfWicket A list containing details about the fall of wickets during the match.
     * @param umpires A list of umpires who officiated the match.
     * @param matchReferees A list of match referees assigned to the game.
     * @param tvUmpires A list of TV umpires who provided off-field assistance during the match.
     * @param scorers A list of scorers involved in recording match statistics.
     * @param playersOfTheMatch A list of players recognized as players of the match.
     * @param closeOfPlay A list containing details captured at the close of each play session.
     * @param debuts A list of players who made their debut in the match.
     * @param batting A list containing batting details for each player in the match.
     * @param bowling A list containing bowling details for each player in the match.
     * @param partnerships A list of details about partnerships formed during the match.
     * @return A populated ScorecardDto object representing the completed scorecard for the match.
     */
    private fun createScoreCard(
        matchData: LocalMatchDto,
        innings: List<LocalInningsDto>,
        fallOfWicket: List<LocalFowDto>,
        umpires: List<PersonDto>,
        matchReferees: List<PersonDto>,
        tvUmpires: List<PersonDto>,
        scorers: List<PersonDto>,
        playersOfTheMatch: List<PersonDto>,
        closeOfPlay: List<CloseOfPlayDto>,
        debuts: List<DebutDto>,
        batting: List<LocalBattingDetailDto>,
        bowling: List<LocalBowlingDetailDto>,
        partnerships: List<LocalPartnershipDetails>
    ): ScorecardDto {

        val tossteam =
            if (matchData.tossTeamId != null)
                ScorecardTeamDto(matchData.tossTeamId, matchData.toss)
            else null
        val where = LocationDto(matchData.locationId, matchData.location)

        val result = ResultDto(
            if (matchData.whoWonId != null) ScorecardTeamDto(matchData.whoWonId, matchData.whoWon!!) else null,
            if (matchData.whoLostId != null) ScorecardTeamDto(matchData.whoLostId, matchData.whoLost!!) else null,
            resultString = matchData.resultString,
            victoryType = VictoryType.entries[matchData.victoryType]
        )

        val awayTeam = ScorecardTeamDto(matchData.awayTeamId, matchData.awayTeamName)
        val homeTeam = ScorecardTeamDto(matchData.homeTeamId, matchData.homeTeamName)

        val scorecardHeaderDto = ScorecardHeaderDto(
            tossteam,
            where,
            result,
            scorers,
            umpires,
            awayTeam,
            listOf(),
            matchData.dayNight,
            homeTeam,
            listOf(),
            tvUmpires,
            matchData.matchDate,
            matchData.matchType,
            matchData.matchTitle,
            matchData.seriesDate,
            closeOfPlay,
            playersOfTheMatch,
            matchData.ballsPerOver,
            matchReferees,
            matchData.matchDesignator
        )

        val battingDtos = createBatting(batting)
        val bowlingDtos = createBowling(bowling)
        val (fowDtos, partnershipDtos) = createFow(fallOfWicket, partnerships)

        val scorecard = ScorecardDto(
            header = scorecardHeaderDto,
            debuts = debuts,
            innings = innings.map { inning ->
                InningDto(
                    team = ScorecardTeamDto(inning.homeTeamId, inning.homeTeamName),
                    opponents = ScorecardTeamDto(inning.awayTeamId, inning.awayTeamName),
                    inningsNumber = inning.inningsNumber,
                    inningsOrder = inning.inningsOrder,
                    declared = inning.declared,
                    complete = inning.complete,
                    TotalDto(
                        inning.wickets, inning.declared, inning.overs, inning.minutes,
                        inning.total
                    ),
                    ExtrasDto(
                        inning.byes, inning.legByes, inning.wides, inning.noballs,
                        inning.penalty,
                        inning.extras
                    ),
                    battingLines = if (battingDtos.size > inning.inningsOrder)
                        battingDtos[inning.inningsOrder]
                    else
                        listOf(),
                    bowlingLines = if (bowlingDtos.size > inning.inningsOrder)
                        bowlingDtos[inning.inningsOrder]
                    else
                        listOf(),
                    fallOfWickets = if (fowDtos.size > inning.inningsOrder)
                        fowDtos[inning.inningsOrder]
                    else
                        listOf(),
                    partnershipDetails = if (partnershipDtos.size > inning.inningsOrder)
                        partnershipDtos[inning.inningsOrder]
                    else
                        listOf()
                )
            },

            )

        val homeTeamScores = getScores(scorecard.innings, scorecard.header.homeTeam.key)
        val awayTeamScores = getScores(scorecard.innings, scorecard.header.awayTeam.key)

        val scoreCardHeader = scorecard.header.copy(homeTeamScores = homeTeamScores, awayTeamScores = awayTeamScores)

        return scorecard.copy(header = scoreCardHeader)
    }

    /**
     * Converts a list of local batting details into a structured list of batting line DTOs,
     * grouped by innings order.
     *
     * @param batting The list of local batting details containing player performance data.
     * @return A list of lists, where each inner list represents the batting details for a specific innings.
     */
    private fun createBatting(batting: List<LocalBattingDetailDto>): List<List<BattingLineDto>> {
        val battingDtos = mutableListOf<List<BattingLineDto>>()

        if(batting.isEmpty()) return battingDtos

        var inningsOrder = -1
        val maxInningsOrder = batting.maxBy { b -> b.inningsOrder }.inningsOrder
        while (++inningsOrder <= maxInningsOrder) {
            val order = inningsOrder

            battingDtos.add(batting.filter { b -> b.inningsOrder == order }
                .map { b ->
                    BattingLineDto(
                        PersonDto(b.playerId, b.playerName),
                        b.score,
                        b.balls,
                        b.fours,
                        b.sixes,
                        b.notOut,
                        b.minutes,
                        b.position,
                        DismissalDto(
                            b.dismissalType, b.dismissal,
                            PersonDto(b.bowlerId, b.bowlerName ?: ""),
                            PersonDto(b.fielderId, b.fielderName ?: "")
                        ),
                        b.captain,
                        b.wicketKeeper
                    )
                }
            )
        }
        return battingDtos
    }

    /**
     * Transforms a list of LocalBowlingDetailDto into a nested list of BowlingLineDto, grouping by innings order.
     *
     * @param bowling A list of LocalBowlingDetailDto containing details of bowling for each player.
     * @return A list of lists, where each inner list represents the transformed bowling details for a specific innings order.
     */
    private fun createBowling(bowling: List<LocalBowlingDetailDto>): List<List<BowlingLineDto>> {
        val bowlingDtos = mutableListOf<List<BowlingLineDto>>()

        if(bowling.isEmpty()) return bowlingDtos

        var inningsOrder = -1
        val maxInningsOrder = bowling.maxBy { b -> b.inningsOrder }.inningsOrder
        while (++inningsOrder <= maxInningsOrder) {
            val order = inningsOrder

            bowlingDtos.add(bowling.filter { b -> b.inningsOrder == order && b.balls != null && b.balls > 0 }
                .map { b ->
                    BowlingLineDto(
                        b.dots,
                        b.runs,
                        b.balls,
                        b.fours,
                        b.sixes,
                        b.overs,
                        b.wides,
                        PersonDto(b.playerId, b.fullName),
                        b.maidens,
                        b.noBalls,
                        b.wickets,
                        b.captain
                    )
                }
            )
        }
        return bowlingDtos
    }

    /**
     * Transforms a list of fall of wicket details and partnership details from their local data transfer forms
     * into structured DTOs, organized by innings order.
     *
     * @param fow A list of `LocalFowDto` objects representing the fall of wickets information.
     * @param partnershipDetails A list of `LocalPartnershipDetails` objects representing the partnership details.
     * @return A pair of mutable lists where the first component is a list of lists of `FallOfWicketDto` objects
     *         and the second component is a list of lists of `PartnershipDetailsDto` objects, both grouped by innings order.
     */
    private fun createFow(
        fow: List<LocalFowDto>,
        partnershipDetails: List<LocalPartnershipDetails>
    ): Pair<MutableList<List<FallOfWicketDto>>, MutableList<List<PartnershipDetailsDto>>> {
        val fowDtos = mutableListOf<List<FallOfWicketDto>>()

        if(fow.isEmpty()) return Pair(fowDtos, mutableListOf())
        if(partnershipDetails.isEmpty()) return Pair(fowDtos, mutableListOf())

        var inningsOrder = -1
        val partnershipDetailsDtos = mutableListOf<List<PartnershipDetailsDto>>()
        val maxInningsOrder = fow.maxBy { b -> b.inningsOrder }.inningsOrder
        while (++inningsOrder <= maxInningsOrder) {
            val order = inningsOrder

            fowDtos.add(fow.filter { b -> b.inningsOrder == order }
                .map { f ->
                    FallOfWicketDto(
                        f.wicket, f.currentScore, PersonDto(f.playerId, f.playerName), f.overs
                    )
                }
            )
            partnershipDetailsDtos.add(partnershipDetails.filter { b -> b.inningsOrder == order }
                .map { pd ->
                    PartnershipDetailsDto(
                        pd.wicket,
                        pd.playerNames,
                        pd.partnership,
                        pd.unbroken != 0,
                        pd.team,
                        pd.opponents,
                        pd.previousScore,
                        pd.previousWicket,
                        pd.multiple,
                        pd.partial
                    )
                }
            )
        }
        return Pair(fowDtos, partnershipDetailsDtos)
    }

    /**
     * Finds a match with the specified match ID from the database and retrieves its details.
     *
     * @param context the DSLContext used for database interaction.
     * @param matchId the unique match identifier (CaId) to search for.
     * @return an Either containing a FindError if the match could not be found, or a LocalMatchDto with match details if successful.
     */
    private fun findMatchWithId(context: DSLContext, matchId: CaId): Either<FindError, LocalMatchDto> {
        val record = context.select(
            MATCHES.ID,
            MATCHES.HOMETEAMID,
            MATCHES.HOMETEAMNAME,
            MATCHES.AWAYTEAMID,
            MATCHES.AWAYTEAMNAME,
            MATCHES.MATCHDESIGNATOR,
            MATCHES.MATCHTITLE,
            MATCHES.LOCATIONID,
            MATCHES.LOCATION.`as`("ground"),
            MATCHES.MATCHDATE,
            MATCHES.SERIESDATE,
            MATCHES.RESULTSTRING,
            MATCHES.BALLSPEROVER,
            MATCHES.DAYNIGHT,
            MATCHES.TOSSTEAMID,
            MATCHES.WHOWONID,
            MATCHES.WHOLOSTID,
            MATCHES.VICTORYTYPE,
            MATCHES.MATCHTYPE,
            field("T.Name", String::class.java).`as`("Toss"),
            field("W.Name", String::class.java).`as`("WhoWon"),
            field("L.Name", String::class.java).`as`("WhoLost"),
        ).from(MATCHES)
            .join(TEAMS.`as`("T")).on(MATCHES.TOSSTEAMID.eq(field("T.Id", Int::class.java)))
            .leftJoin(TEAMS.`as`("W")).on(MATCHES.WHOWONID.eq(field("W.Id", Int::class.java)))
            .leftJoin(TEAMS.`as`("L")).on(MATCHES.WHOLOSTID.eq(field("L.Id", Int::class.java)))
            .where(MATCHES.CAID.eq(matchId.id))
            .fetchOne()

        if (record == null) return FindError("Can't find match with matchid $matchId").left()


        return LocalMatchDto(
            matchId = record.get("Id", Int::class.java),
            homeTeamId = record.get("HomeTeamId", Int::class.java),
            homeTeamName = record.get("HomeTeamName", String::class.java),
            awayTeamId = record.get("AwayTeamId", Int::class.java),
            awayTeamName = record.get("AwayTeamName", String::class.java),
            matchDesignator = record.get("MatchDesignator", String::class.java),
            matchTitle = record.get("MatchTitle", String::class.java),
            locationId = record.get("LocationId", Int::class.java),
            location = record.get("ground", String::class.java),
            matchDate = record.get("MatchDate", String::class.java),
            seriesDate = record.get("SeriesDate", String::class.java),
            resultString = record.get("ResultString", String::class.java),
            ballsPerOver = record.get("BallsPerOver", Int::class.java),
            dayNight = record.get("DayNight", Boolean::class.java),
            tossTeamId = record.get("TossTeamId", Int::class.java),
            whoWonId = record.getValueOrNull("WhoWonId", String::class.java)?.toInt(),
            whoLostId = record.getValueOrNull("WhoLostId", String::class.java)?.toInt(),
            victoryType = record.get("VictoryType", Int::class.java),
            matchType = record.get("MatchType", String::class.java),
            toss = record.get("Toss", String::class.java),
            whoWon = record.getValueOrNull("WhoWon", String::class.java),
            whoLost = record.getValueOrNull("WhoLost", String::class.java)
        ).right()
    }

    /**
     * Finds a match based on the given home team name, away team name, and match start date.
     *
     * @param context the DSLContext used to execute the database query
     * @param homeTeamName the name of the home team to search for
     * @param awayTeamName the name of the away team to search for
     * @param matchStartDate the start date of the match to search for
     * @return either a [FindError] if the match is not found or a [LocalMatchDto] containing the match details
     */
    private fun findMatch(
        context: DSLContext,
        homeTeamName: String,
        awayTeamName: String,
        matchStartDate: String
    ): Either<FindError, LocalMatchDto> {
        val record = context.select(
            MATCHES.ID,
            MATCHES.HOMETEAMID,
            MATCHES.HOMETEAMNAME,
            MATCHES.AWAYTEAMID,
            MATCHES.AWAYTEAMNAME,
            MATCHES.MATCHDESIGNATOR,
            MATCHES.MATCHTITLE,
            MATCHES.LOCATIONID,
            MATCHES.LOCATION.`as`("ground"),
            MATCHES.MATCHDATE,
            MATCHES.SERIESDATE,
            MATCHES.RESULTSTRING,
            MATCHES.BALLSPEROVER,
            MATCHES.DAYNIGHT,
            MATCHES.TOSSTEAMID,
            MATCHES.WHOWONID,
            MATCHES.WHOLOSTID,
            MATCHES.VICTORYTYPE,
            MATCHES.MATCHTYPE,
            field("T.Name", String::class.java).`as`("Toss"),
            field("W.Name", String::class.java).`as`("WhoWon"),
            field("L.Name", String::class.java).`as`("WhoLost"),
        ).from(MATCHES)
            .join(TEAMS.`as`("T")).on(MATCHES.TOSSTEAMID.eq(field("T.Id", Int::class.java)))
            .join(TEAMS.`as`("W")).on(MATCHES.WHOWONID.eq(field("W.Id", Int::class.java)))
            .join(TEAMS.`as`("L")).on(MATCHES.WHOLOSTID.eq(field("L.Id", Int::class.java)))
            .where(MATCHES.HOMETEAMNAME.eq(homeTeamName))
            .and(MATCHES.AWAYTEAMNAME.eq(awayTeamName))
            .and(field("MATCHES.MATCHSTARTDATE", String::class.java).eq(matchStartDate))
            .fetchOne()

        if (record == null) return FindError("Can't find match with homeTeamName $homeTeamName and awayTeamName $awayTeamName and matchStartDate $matchStartDate").left()


        return LocalMatchDto(
            matchId = record.get("Id", Int::class.java),
            homeTeamId = record.get("HomeTeamId", Int::class.java),
            homeTeamName = record.get("HomeTeamName", String::class.java),
            awayTeamId = record.get("AwayTeamId", Int::class.java),
            awayTeamName = record.get("AwayTeamName", String::class.java),
            matchDesignator = record.get("MatchDesignator", String::class.java),
            matchTitle = record.get("MatchTitle", String::class.java),
            locationId = record.get("LocationId", Int::class.java),
            location = record.get("Location", String::class.java),
            matchDate = record.get("MatchDate", String::class.java),
            seriesDate = record.get("SeriesDate", String::class.java),
            resultString = record.get("ResultString", String::class.java),
            ballsPerOver = record.get("BallsPerOver", Int::class.java),
            dayNight = record.get("DayNight", Boolean::class.java),
            tossTeamId = record.get("TossTeamId", Int::class.java),
            whoWonId = record.getValueOrNull("WhoWonId", Int::class.java),
            whoLostId = record.getValueOrNull("WhoLostId", Int::class.java),
            victoryType = record.get("VictoryType", Int::class.java),
            matchType = record.get("MatchType", String::class.java),
            toss = record.get("Toss", String::class.java),
            whoWon = record.getValueOrNull("WhoWon", String::class.java),
            whoLost = record.getValueOrNull("WhoLost", String::class.java)
        ).right()
    }

    /**
     * Retrieves a list of innings details for a given match.
     *
     * @param context The DSLContext used to execute the database query.
     * @param matchId The ID of the match whose innings details are to be retrieved.
     * @return Either a FindError if an error occurs during retrieval, or a list of LocalInningsDto containing the innings details.
     */
    private fun getInnings(context: DSLContext, matchId: Int): Either<FindError, List<LocalInningsDto>> {
        val records = context.select(
            INNINGS.TEAMID.`as`("homeTeamId"),
            field("t.name").`as`("homeTeamName"),
            INNINGS.OPPONENTSID.`as`("awayTeamId"),
            field("o.name").`as`("awayTeamName"),
            INNINGS.INNINGSNUMBER,
            INNINGS.INNINGSORDER,
            INNINGS.TOTAL,
            INNINGS.WICKETS,
            INNINGS.COMPLETE,
            INNINGS.MINUTES,
            INNINGS.BYES,
            INNINGS.LEGBYES,
            INNINGS.WIDES,
            INNINGS.NOBALLS,
            INNINGS.PENALTY,
            INNINGS.EXTRAS,
            INNINGS.OVERS,
            INNINGS.DECLARED,
        )
            .from(INNINGS)
            .join(TEAMS.`as`("T")).on(field("T.id").eq(INNINGS.TEAMID))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(INNINGS.OPPONENTSID))
            .where(INNINGS.MATCHID.eq(matchId))
            .fetch()

//        if (records.isEmpty()) return FindError("Invalid matchid $matchId").left()

        val innings = mutableListOf<LocalInningsDto>()

        records.forEach { record ->
            innings.add(
                LocalInningsDto(
                    homeTeamId = record.get("homeTeamId", Int::class.java),
                    homeTeamName = record.get("homeTeamName", String::class.java),
                    awayTeamId = record.get("awayTeamId", Int::class.java),
                    awayTeamName = record.get("awayTeamName", String::class.java),
                    inningsNumber = record.get("InningsNumber", Int::class.java),
                    inningsOrder = record.get("InningsOrder", Int::class.java),
                    total = record.get("Total", Int::class.java),
                    wickets = record.get("Wickets", Int::class.java),
                    complete = record.get("Complete", Boolean::class.java),
                    minutes = record.get("Minutes", Int::class.java),
                    byes = record.get("Byes", Int::class.java),
                    legByes = record.get("LegByes", Int::class.java),
                    wides = record.get("Wides", Int::class.java),
                    noballs = record.get("Noballs", Int::class.java),
                    penalty = record.get("Penalty", Int::class.java),
                    extras = record.get("Extras", Int::class.java),
                    overs = record.get("Overs", String::class.java),
                    declared = record.get("Declared", Boolean::class.java),

                    )
            )
        }

        return innings.right()
    }

    /**
     * Retrieves the fall of wickets for a specific match.
     *
     * @param context DSLContext used for executing database queries.
     * @param matchId The unique identifier of the match whose fall of wickets data is to be fetched.
     * @return Either a FindError if the operation fails or a list of LocalFowDto containing fall of wickets information.
     */
    private fun getFallOfWickets(context: DSLContext, matchId: Int): Either<FindError, List<LocalFowDto>> {
        val records = context.select(
            FALLOFWICKETS.MATCHID,
            FALLOFWICKETS.INNINGSORDER,
            FALLOFWICKETS.WICKET,
            FALLOFWICKETS.PLAYERID,
            PLAYERS.FULLNAME.`as`("PlayerName"),
            FALLOFWICKETS.CURRENTSCORE,
            FALLOFWICKETS.OVERS
        ).from(FALLOFWICKETS)
            .join(PLAYERS).on(FALLOFWICKETS.PLAYERID.eq(PLAYERS.ID))
            .where(FALLOFWICKETS.MATCHID.eq(matchId))
            .fetch()

        val fallOfWickets = mutableListOf<LocalFowDto>()

        records.forEach { record ->
            fallOfWickets.add(
                LocalFowDto(
                    matchId = record.get("MatchId", Int::class.java),
                    inningsOrder = record.get("InningsOrder", Int::class.java),
                    wicket = record.get("Wicket", Int::class.java),
                    playerId = record.get("PlayerId", Int::class.java),
                    playerName = record.get("PlayerName", String::class.java),
                    currentScore = record.get("CurrentScore", Int::class.java),
                    overs = record.get("Overs", String::class.java)
                )
            )
        }

        return fallOfWickets.right()
    }

    /**
     * Fetches a list of officials associated with a specific match from the database.
     *
     * @param context The database DSLContext used to execute the query.
     * @param matchId The unique identifier of the match for which officials are to be fetched.
     * @param matchesTable The name of the table containing match details.
     * @param officialsTable The name of the table containing official details.
     * @return Either a FindError in case of failure or a list of PersonDto representing the officials.
     */
    private fun getOfficials(
        context: DSLContext,
        matchId: Int,
        matchesTable: String,
        officialsTable: String
    ): Either<FindError, List<PersonDto>> {
        val records = context.select(
            field("${officialsTable}.id", Int::class.java).`as`("key"),
            field("${officialsTable}.fullname", String::class.java).`as`("name"),
        ).from(matchesTable)
            .join(officialsTable)
            .on(field("${officialsTable}.id", Int::class.java).eq(field("${matchesTable}.PersonId", Int::class.java)))
            .where(field("${matchesTable}.matchid", Int::class.java).eq(matchId))
            .fetch()

        val officials = mutableListOf<PersonDto>()

        records.forEach { record ->
            officials.add(PersonDto(record.get("key", Int::class.java), record.get("name", String::class.java)))
        }

        return officials.right()
    }

    /**
     * Retrieves a list of players who were awarded "Player of the Match" for a given match.
     *
     * @param context The DSLContext used to interact with the database.
     * @param matchId The unique identifier of the match.
     * @return Either a `FindError` if an error occurs during retrieval, or a `List` of `PersonDto` containing details of the players of the match.
     */
    private fun getPlayerOfTheMatch(context: DSLContext, matchId: Int): Either<FindError, List<PersonDto>> {
        val records = context.select(
            PLAYERS.FULLNAME.`as`("name"),
            PLAYERS.ID.`as`("key"),
        ).from(PLAYERS).join(PLAYERSOFTHEMATCHMATCHES).on(PLAYERS.ID.eq(PLAYERSOFTHEMATCHMATCHES.PERSONID))
            .where(PLAYERSOFTHEMATCHMATCHES.MATCHID.eq(matchId))
            .fetch()

        val players = mutableListOf<PersonDto>()

        records.forEach { record ->
            players.add(PersonDto(record.get("key", Int::class.java), record.get("name", String::class.java)))
        }

        return players.right()
    }

    /**
     * Retrieves the close of play details for a specified match.
     *
     * @param context The DSLContext used to execute the database query.
     * @param matchId The unique identifier of the match for which close of play details are to be fetched.
     * @return Either an Error object in case of failure, or a list of CloseOfPlayDto objects containing the close of play details on success.
     */
    private fun getCloseOfPlay(context: DSLContext, matchId: Int): Either<Error, List<CloseOfPlayDto>> {

        val records =
            context.select(
                CLOSEOFPLAY.DAY,
                CLOSEOFPLAY.NOTE
            ).from(CLOSEOFPLAY).where(CLOSEOFPLAY.MATCHID.eq(matchId))
                .fetch()

        val closeOfPlay = mutableListOf<CloseOfPlayDto>()
        records.forEach {
            closeOfPlay.add(
                CloseOfPlayDto(
                    day = it.get("Day", Int::class.java),
                    note = it.get("Note", String::class.java)
                )
            )
        }

        return closeOfPlay.right()
    }

    /**
     * Retrieves a list of notes associated with a specific match ID from the database.
     *
     * @param context The DSLContext used to execute the query.
     * @param matchId The ID of the match for which notes are to be retrieved.
     * @return Either an Error if the query fails or a List of String containing the notes.
     */
    private fun getNotes(context: DSLContext, matchId: Int): Either<Error, List<String>> {

        val records =
            context.select(
                NOTES.NOTE,
            ).from(NOTES).where(NOTES.MATCHID.eq(matchId)).fetch()

        val notes = mutableListOf<String>()

        records.forEach { record ->
            notes.add(record.get("Note", String::class.java))
        }

        return notes.right()
    }

    /**
     * Retrieves a list of debuts for a specific match and match type from the database.
     *
     * @param context The DSLContext used for querying the database.
     * @param matchId The unique identifier of the match for which debuts are to be fetched.
     * @param matchType The type of match (e.g., Test, ODI, T20) to filter debuts.
     * @return Either an Error object if an error occurs, or a list of DebutDto containing debut details.
     */
    private fun getDebuts(context: DSLContext, matchId: Int, matchType: String): Either<Error, List<DebutDto>> {
        val records =
            context.select(
                PLAYERS.PLAYERID,
                PLAYERS.FULLNAME,
                PLAYERS.SORTNAMEPART,
                TEAMS.TEAMID,
                TEAMS.NAME.`as`("TeamName")
            ).from(PLAYERS)
                .join(PLAYERSTEAMS).on(PLAYERS.ID.eq(PLAYERSTEAMS.PLAYERID))
                .join(TEAMS).on(PLAYERSTEAMS.TEAMID.eq(TEAMS.ID))
                .where(PLAYERSTEAMS.DEBUTID.eq(matchId))
                .and(PLAYERSTEAMS.MATCHTYPE.eq(matchType))
                .fetch()

        val debuts = mutableListOf<DebutDto>()

        records.forEach { record ->
            debuts.add(
                DebutDto(
                    playerId = record.get("PlayerId", Int::class.java),
                    fullName = record.get("FullName", String::class.java),
                    teamId = record.get("TeamId", Int::class.java),
                    teamName = record.get("TeamName", String::class.java)
                )
            )
        }

        return debuts.right()
    }

    /**
     * Fetches the batting details for a specific match from the database.
     *
     * @param context the DSLContext used for executing queries
     * @param matchId the identifier of the match for which batting details are to be retrieved
     * @return an `Either` containing a `FindError` on failure or a list of `LocalBattingDetailDto` on success
     */
    private fun getBatting(context: DSLContext, matchId: Int): Either<FindError, List<LocalBattingDetailDto>> {

        val records = context.select(
            BATTINGDETAILS.PLAYERID,
            BATTINGDETAILS.FULLNAME.`as`("PlayerName"),
            BATTINGDETAILS.INNINGSNUMBER,
            BATTINGDETAILS.INNINGSORDER,
            BATTINGDETAILS.DISMISSAL,
            BATTINGDETAILS.DISMISSALTYPE,
            BATTINGDETAILS.BOWLERID,
            BATTINGDETAILS.BOWLERNAME,
            BATTINGDETAILS.FIELDERID,
            BATTINGDETAILS.FIELDERNAME,
            BATTINGDETAILS.SCORE,
            BATTINGDETAILS.POSITION,
            BATTINGDETAILS.NOTOUT,
            BATTINGDETAILS.BALLS,
            BATTINGDETAILS.MINUTES,
            BATTINGDETAILS.FOURS,
            BATTINGDETAILS.SIXES,
            BATTINGDETAILS.CAPTAIN,
            BATTINGDETAILS.WICKETKEEPER,
        ).from(BATTINGDETAILS)
            .where(BATTINGDETAILS.MATCHID.eq(matchId))
            .orderBy(BATTINGDETAILS.INNINGSORDER, BATTINGDETAILS.POSITION)
            .fetch()

        val battingDetails = mutableListOf<LocalBattingDetailDto>()

        records.forEach { record ->
            battingDetails.add(
                LocalBattingDetailDto(
                    playerId = record.get("PlayerId", Int::class.java),
                    playerName = record.get("PlayerName", String::class.java),
                    inningsNumber = record.get("InningsNumber", Int::class.java),
                    inningsOrder = record.get("InningsOrder", Int::class.java),
                    dismissal = record.get("Dismissal", String::class.java),
                    dismissalType = record.get("DismissalType", Int::class.java),
                    bowlerId = record.get("BowlerId", Int::class.java),
                    bowlerName = record.getValueOrNull("BowlerName", String::class.java),
                    fielderId = record.get("FielderId", Int::class.java),
                    fielderName = record.getValueOrNull("FielderName", String::class.java),
                    score = record.getValueOrNull("Score", String::class.java)?.toInt(),
                    position = record.get("Position", Int::class.java),
                    notOut = record.get("NotOut", Boolean::class.java),
                    balls = record.getValueOrNull("Balls", String::class.java)?.toInt(),
                    minutes = record.getValueOrNull("Minutes", String::class.java)?.toInt(),
                    fours = record.getValueOrNull("Fours", String::class.java)?.toInt(),
                    sixes = record.getValueOrNull("Sixes", String::class.java)?.toInt(),
                    captain = record.get("Captain", Boolean::class.java),
                    wicketKeeper = record.get("WicketKeeper", Boolean::class.java),
                )
            )
        }

        return battingDetails.right()
    }

    /**
     * Retrieves bowling details for a specific match from the database.
     *
     * @param context the DSLContext used to interact with the database
     * @param matchId the ID of the match for which bowling details are to be retrieved
     * @return an Either containing a FindError on the left in case of failure, or a list of LocalBowlingDetailDto on the right with the retrieved bowling details
     */
    private fun getBowling(context: DSLContext, matchId: Int): Either<FindError, List<LocalBowlingDetailDto>> {

        val records = context.select(
            BOWLINGDETAILS.PLAYERID,
            BOWLINGDETAILS.NAME.`as`("FullName"),
            BOWLINGDETAILS.INNINGSORDER,
            BOWLINGDETAILS.OVERS,
            BOWLINGDETAILS.BALLS,
            BOWLINGDETAILS.RUNS,
            BOWLINGDETAILS.MAIDENS,
            BOWLINGDETAILS.WICKETS,
            BOWLINGDETAILS.FOURS,
            BOWLINGDETAILS.SIXES,
            BOWLINGDETAILS.DOTS,
            BOWLINGDETAILS.WIDES,
            BOWLINGDETAILS.NOBALLS,
            BOWLINGDETAILS.CAPTAIN,
        ).from(BOWLINGDETAILS)
            .where(BOWLINGDETAILS.MATCHID.eq(matchId))
            .orderBy(BOWLINGDETAILS.INNINGSORDER, BOWLINGDETAILS.POSITION)
            .fetch()

        val bowlingDetails = mutableListOf<LocalBowlingDetailDto>()

        records.forEach { record ->
            bowlingDetails.add(
                LocalBowlingDetailDto(
                    playerId = record.get("PlayerId", Int::class.java),
                    fullName = record.get("FullName", String::class.java),
                    inningsOrder = record.get("InningsOrder", Int::class.java),
                    overs = record.get("Overs", String::class.java),
                    balls = record.get("Balls", Int::class.java),
                    runs = record.getValueOrNull("Runs", String::class.java)?.toInt(),
                    maidens = record.getValueOrNull("Maidens", String::class.java)?.toInt(),
                    wickets = record.getValueOrNull("Wickets", String::class.java)?.toInt(),
                    fours = record.getValueOrNull("Fours", String::class.java)?.toInt(),
                    sixes = record.getValueOrNull("Sixes", String::class.java)?.toInt(),
                    dots = record.getValueOrNull("Dots", String::class.java)?.toInt(),
                    wides = record.getValueOrNull("Wides", String::class.java)?.toInt(),
                    noBalls = record.getValueOrNull("NoBalls", String::class.java)?.toInt(),
                    captain = record.get("Captain", Boolean::class.java),
                )
            )
        }

        return bowlingDetails.right()
    }

    /**
     * Retrieves the partnerships for a specific match based on the given match ID.
     *
     * @param context The DSLContext used to perform the database operations.
     * @param matchId The unique identifier of the match for which partnerships are to be retrieved.
     * @return An Either type containing a FindError in case of failure, or a list of LocalPartnershipDetails on success.
     */
    private fun getPartnerships(
        context: DSLContext,
        matchId: Int
    ): Either<FindError, List<LocalPartnershipDetails>> {
        val records = context.select(
            PARTNERSHIPS.INNINGSORDER,
            PARTNERSHIPS.WICKET,
            PARTNERSHIPS.PLAYERNAMES,
            PARTNERSHIPS.PARTNERSHIP,
            PARTNERSHIPS.UNBROKEN,
            field("T.name").`as`("Team"),
            field("O.name").`as`("Opponents"),
            PARTNERSHIPS.PREVIOUSSCORE,
            PARTNERSHIPS.PREVIOUSWICKET,
            PARTNERSHIPS.MULTIPLE,
            PARTNERSHIPS.PARTIAL,
        ).from(PARTNERSHIPS)
            .join(TEAMS.`as`("T")).on(field("T.id").eq(PARTNERSHIPS.TEAMID))
            .join(TEAMS.`as`("O")).on(field("O.id").eq(PARTNERSHIPS.OPPONENTSID))
            .where(PARTNERSHIPS.MATCHID.eq(matchId))
            .orderBy(PARTNERSHIPS.INNINGSORDER, PARTNERSHIPS.WICKET).fetch()

        val partnerships = mutableListOf<LocalPartnershipDetails>()

        records.forEach { record ->
            partnerships.add(
                LocalPartnershipDetails(
                    inningsOrder = record.get("InningsOrder", Int::class.java),
                    wicket = record.get("Wicket", Int::class.java),
                    playerNames = record.get("PlayerNames", String::class.java),
                    partnership = record.get("Partnership", Int::class.java),
                    unbroken = record.get("Unbroken", Int::class.java),
                    team = record.get("Team", String::class.java),
                    opponents = record.get("Opponents", String::class.java),
                    previousWicket = record.get("PreviousWicket", Int::class.java),
                    previousScore = record.get("PreviousScore", Int::class.java),
                    multiple = record.get("Multiple", Boolean::class.java),
                    partial = record.get("Partial", Boolean::class.java),

                    )
            )
        }

        return partnerships.right()

    }

    /**
     * Filters and generates a list of formatted score strings for a specified team based on their key.
     *
     * @param innings A list of `InningDto` objects containing details of each inning in the match.
     * @param key The unique key of the team for which scores are to be retrieved.
     * @return A list of formatted strings representing the scores of the specified team. Each string may include:
     * - Total runs with wickets, optionally suffixed with "d" if the innings was declared.
     * - Total runs and wickets in the format "total for wickets" if wickets are less than 10.
     * - Total runs alone if no other condition is met.
     */
    private fun getScores(innings: List<InningDto>, key: Int): List<String> {
        return innings
            .filter { i -> i.team.key == key }
            .map { i ->
                if (i.total.declared)
                    "${i.total.total}/${i.total.wickets} d"
                if (i.total.wickets < 10)
                    "${i.total.total} for ${i.total.wickets}"
                i.total.total.toString()
            }

    }
}

/**
 * Represents the data transfer object for a local match entity.
 *
 * This class is used to store and transfer information about a cricket match, including details such as team names,
 * match location, match date, results, and other metadata related to the match. It serves as an input or output
 * object in operations involving match-related data, particularly in database interactions or API responses.
 *
 * @property matchId Unique identifier for the match.
 * @property homeTeamId Identifier for the home team.
 * @property homeTeamName Name of the home team.
 * @property awayTeamId Identifier for the away team.
 * @property awayTeamName Name of the away team.
 * @property matchDesignator Designator or symbolic identifier for the match.
 * @property matchTitle Title or name of the match for display purposes.
 * @property location Name of the match location or venue.
 * @property locationId Identifier for the match location.
 * @property matchDate Date of the match in string format.
 * @property seriesDate Date of the series to which the match belongs, in string format.
 * @property resultString Textual representation of the match result.
 * @property ballsPerOver Number of balls bowled per over in the match.
 * @property dayNight Indicates if the match is a day-night match.
 * @property tossTeamId Identifier for the team that won the toss, nullable if no toss occurred.
 * @property toss Textual representation of the toss result.
 * @property whoWonId Identifier for the team that won the match, nullable if no winner.
 * @property whoWon Name of the team that won the match, nullable if no winner.
 * @property whoLostId Identifier for the team that lost the match, nullable if no loser.
 * @property whoLost Name of the team that lost the match, nullable if no loser.
 * @property victoryType Indicator of the type of victory (e.g., margin of runs or wickets).
 * @property matchType Type of the match (e.g., Test, ODI, T20).
 */
private data class LocalMatchDto(
    val matchId: Int,
    val homeTeamId: Int,
    val homeTeamName: String,
    val awayTeamId: Int,
    val awayTeamName: String,
    val matchDesignator: String,
    val matchTitle: String,
    val location: String,
    val locationId: Int,
    val matchDate: String,
    val seriesDate: String,
    val resultString: String,
    val ballsPerOver: Int,
    val dayNight: Boolean,
    val tossTeamId: Int?,
    val toss: String,
    val whoWonId: Int?,
    val whoWon: String?,
    val whoLostId: Int?,
    val whoLost: String?,
    val victoryType: Int,
    val matchType: String
)

/**
 * Represents the details of an innings in a cricket match.
 *
 * @property homeTeamId Identifier of the home team involved in this innings.
 * @property homeTeamName Name of the home team.
 * @property awayTeamId Identifier of the away team involved in this innings.
 * @property awayTeamName Name of the away team.
 * @property inningsNumber The number of this innings in the match (e.g., 1 for the first innings, 2 for the second).
 * @property inningsOrder The order of the innings within the match (could represent the batting or bowling turn).
 * @property total The total runs scored in this innings.
 * @property wickets The number of wickets lost in this innings.
 * @property complete Indicates whether the innings has been completed.
 * @property minutes The total minutes this innings lasted.
 * @property byes The runs awarded as byes in the innings.
 * @property legByes The runs awarded as leg-byes in the innings.
 * @property wides The number of wides bowled during the innings.
 * @property noballs The number of no-balls bowled during the innings.
 * @property penalty The number of penalty runs awarded during the innings.
 * @property extras The total number of extra runs (sum of byes, leg-byes, wides, no-balls, and penalty).
 * @property overs The number of overs bowled in this innings (generally in the format "overs.balls").
 * @property declared Indicates if the innings was declared closed by the team.
 */
private data class LocalInningsDto(
    val homeTeamId: Int,
    val homeTeamName: String,
    val awayTeamId: Int,
    val awayTeamName: String,
    val inningsNumber: Int,
    val inningsOrder: Int,
    val total: Int,
    val wickets: Int,
    val complete: Boolean,
    val minutes: Int,
    val byes: Int,
    val legByes: Int,
    val wides: Int,
    val noballs: Int,
    val penalty: Int,
    val extras: Int,
    val overs: String,
    val declared: Boolean
)

/**
 * Represents the "fall of wicket" data for a specific match and inning.
 *
 * @property matchId The unique identifier of the match.
 * @property inningsOrder The inning number in which the wicket fell.
 * @property wicket The sequence number of the wicket within the inning.
 * @property playerId The unique identifier of the player who got out.
 * @property playerName The name of the player who got out.
 * @property currentScore The score of the team at the moment the wicket fell.
 * @property overs The overs completed at the time of the wicket.
 */
private data class LocalFowDto(
    val matchId: Int,
    val inningsOrder: Int,
    val wicket: Int,
    val playerId: Int,
    val playerName: String,
    val currentScore: Int?,
    val overs: String
)


/**
 * Represents the details of a player's batting performance in a specific innings of a cricket match.
 *
 * @property playerId The unique identifier of the player.
 * @property playerName The name of the player.
 * @property inningsNumber The number of the innings in which this record is applicable.
 * @property inningsOrder The batting order of the player in the innings.
 * @property dismissal A textual description of how the player was dismissed.
 * @property dismissalType An integer code representing the type of dismissal.
 * @property bowlerId The unique identifier of the bowler involved in the dismissal.
 * @property bowlerName The name of the bowler involved in the dismissal, if applicable.
 * @property fielderId The unique identifier of the fielder involved in the dismissal, if applicable.
 * @property fielderName The name of the fielder involved in the dismissal, if applicable.
 * @property score The number of runs scored by the player.
 * @property position The batting position of the player.
 * @property notOut A flag indicating whether the player was not out at the end of the innings.
 * @property balls The number of balls faced by the player.
 * @property minutes The number of minutes spent at the crease by the player.
 * @property fours The number of boundaries (4 runs) hit by the player.
 * @property sixes The number of sixes (6 runs) hit by the player.
 * @property captain A flag indicating whether the player was the captain in this match.
 * @property wicketKeeper A flag indicating whether the player was the wicket-keeper in this match.
 */
private data class LocalBattingDetailDto(
    val playerId: Int,
    val playerName: String,
    val inningsNumber: Int,
    val inningsOrder: Int,
    val dismissal: String,
    val dismissalType: Int,
    val bowlerId: Int,
    val bowlerName: String?,
    val fielderId: Int,
    val fielderName: String?,
    val score: Int?,
    val position: Int,
    val notOut: Boolean,
    val balls: Int?,
    val minutes: Int?,
    val fours: Int?,
    val sixes: Int?,
    val captain: Boolean,
    val wicketKeeper: Boolean,
)

/**
 * Represents detailed bowling data for a player in a specific match context.
 *
 * @property playerId Unique identifier for the player.
 * @property fullName Full name of the player.
 * @property inningsOrder The order of the innings where the player bowled.
 * @property overs Number of overs bowled, represented as a string to capture fractional overs.
 * @property balls Total number of balls bowled by the player (nullable).
 * @property runs Total number of runs conceded by the player (nullable).
 * @property maidens Total number of maiden overs bowled by the player (nullable).
 * @property wickets Total number of wickets taken by the player (nullable).
 * @property fours Total number of boundaries conceded by the player (nullable).
 * @property sixes Total number of sixes conceded by the player (nullable).
 * @property dots Total number of dot balls bowled by the player (nullable).
 * @property wides Total number of wides bowled by the player (nullable).
 * @property noBalls Total number of no-balls bowled by the player (nullable).
 * @property captain Indicates whether the player was the captain during the match.
 */
private data class LocalBowlingDetailDto(
    val playerId: Int,
    val fullName: String,
    val inningsOrder: Int,
    val overs: String,
    val balls: Int?,
    val runs: Int?,
    val maidens: Int?,
    val wickets: Int?,
    val fours: Int?,
    val sixes: Int?,
    val dots: Int?,
    val wides: Int?,
    val noBalls: Int?,
    val captain: Boolean,
)

/**
 * Represents details of a partnership in a cricket match for a specific innings and wicket.
 *
 * @property inningsOrder The order of the innings during the match.
 * @property wicket The specific wicket number associated with this partnership.
 * @property playerNames The names of the players involved in the partnership.
 * @property partnership The total runs scored in this partnership.
 * @property unbroken Indicates the runs scored in unbroken partnerships when no further wickets fell.
 * @property team The name of the team for which the partnership occurred.
 * @property opponents The name of the opposing team during the partnership.
 * @property previousWicket The wicket number before this partnership began.
 * @property previousScore The score at the fall of the previous wicket before this partnership.
 * @property multiple A boolean indicating whether this partnership spans multiple innings.
 * @property partial A boolean indicating if the partnership is partial (not fully complete due to external circumstances).
 */
private data class LocalPartnershipDetails(
    val inningsOrder: Int,
    val wicket: Int,
    val playerNames: String,
    val partnership: Int,
    val unbroken: Int,
    val team: String,
    val opponents: String,
    val previousWicket: Int,
    val previousScore: Int,
    val multiple: Boolean,
    val partial: Boolean,
)