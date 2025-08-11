package com.knowledgespike.feature.frontpage.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.core.type.json.DatabaseResult
import com.knowledgespike.core.type.shared.now
import com.knowledgespike.db.tables.Innings.Companion.INNINGS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.feature.frontpage.domain.model.MatchDetails
import com.knowledgespike.feature.frontpage.domain.repository.FrontPageRepository
import com.knowledgespike.plugins.DataSource
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaLocalDate
import org.jooq.Record15
import org.jooq.Record6
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.select
import org.jooq.impl.DSL.using

class JooqFrontPageRepository(private val dataSource: DataSource) : FrontPageRepository {
    override fun getRecentMatches(): DatabaseResult<MatchDetails> {
        val context = using(dataSource.dataSource, DIALECT)

        val matchEndDate = LocalDate.now().minus(2, DateTimeUnit.DAY)

        val cte1 = createMainCte(matchEndDate)
        val cte2 = createOtherCte(matchEndDate, false, 1)
        val cte3 = createOtherCte(matchEndDate, true, 2)
        val cte4 = createOtherCte(matchEndDate, false, 2)

        val databaseResults = context.select(
            field("cte1.HomeTeamName", String::class.java),
            field("cte1.AwayTeamName", String::class.java),
            field("cte1.ResultString", String::class.java),
            field("cte1.DurationType", String::class.java),
            field("cte1.Tournament", String::class.java),
            field("cte1.MatchType", String::class.java),
            field("cte1.MatchStartDate", String::class.java),
            field("cte1.MatchEndDate", String::class.java),
            field("cte1.Total", String::class.java).`as`("homeTotal1"),
            field("cte1.Wickets", String::class.java).`as`("homeWickets1"),
            field("cte1.Overs", String::class.java).`as`("homeOvers1"),
            field("cte1.Declared", String::class.java).`as`("homeDeclared1"),
            field("cte2.Total", String::class.java).`as`("awayTotal1"),
            field("cte2.Wickets", String::class.java).`as`("awayWickets1"),
            field("cte2.Overs", String::class.java).`as`("awayOvers1"),
            field("cte2.Declared", String::class.java).`as`("awayDeclared1"),
            field("cte3.Total", String::class.java).`as`("homeTotal2"),
            field("cte3.Wickets", String::class.java).`as`("homeWickets2"),
            field("cte3.Overs", String::class.java).`as`("homeOvers2"),
            field("cte3.Declared", String::class.java).`as`("homeDeclared2"),
            field("cte4.Total", String::class.java).`as`("awayTotal2"),
            field("cte4.Wickets", String::class.java).`as`("awayWickets2"),
            field("cte4.Overs", String::class.java).`as`("awayOvers2"),
            field("cte4.Declared", String::class.java).`as`("awayDeclared2"),
        )
            .from(cte1.asTable("cte1"))
            .leftJoin(cte2.asTable("cte2"))
            .on(field("cte1.MatchId").eq(field("cte2.MatchId")))
            .leftJoin(cte3.asTable("cte3"))
            .on(field("cte1.MatchId").eq(field("cte3.MatchId")))
            .leftJoin(cte4.asTable("cte4"))
            .on(field("cte1.MatchId").eq(field("cte4.MatchId")))
            .orderBy(field("cte1.MatchEndDate"), field("cte1.HomeTeamName"))
            .fetch()


        val results = databaseResults.map {

            MatchDetails(
                homeTeam = it.getValue("cte1.HomeTeamName", String::class.java),
                awayTeam = it.getValue("cte1.AwayTeamName", String::class.java),
                resultString = it.getValue("cte1.ResultString", String::class.java),
                durationType = it.getValue("cte1.DurationType", String::class.java),
                tournament = it.getValue("cte1.Tournament", String::class.java),
                matchType = it.getValue("cte1.MatchType", String::class.java),
                homeTotal1 = it.getValue("homeTotal1", Int::class.javaObjectType),
                homeWickets1 = it.getValue("homeWickets1", Int::class.javaObjectType),
                homeOvers1 = it.getValue("homeOvers1", String::class.javaObjectType),
                homeDeclared1 = it.getValue("homeDeclared1", Boolean::class.javaObjectType),
                awayTotal1 = it.getValue("awayTotal1", Int::class.javaObjectType),
                awayWickets1 = it.getValue("awayWickets1", Int::class.javaObjectType),
                awayOvers1 = it.getValue("awayOvers1", String::class.javaObjectType),
                awayDeclared1 = it.getValue("awayDeclared1", Boolean::class.javaObjectType),
                homeTotal2 = it.getValue("homeTotal2", Int::class.javaObjectType),
                homeWickets2 = it.getValue("homeWickets2", Int::class.javaObjectType),
                homeOvers2 = it.getValue("homeOvers2", String::class.javaObjectType),
                homeDeclared2 = it.getValue("homeDeclared2", Boolean::class.javaObjectType),
                awayTotal2 = it.getValue("awayTotal2", Int::class.javaObjectType),
                awayWickets2 = it.getValue("awayWickets2", Int::class.javaObjectType),
                awayOvers2 = it.getValue("awayOvers2", String::class.javaObjectType),
                awayDeclared2 = it.getValue("awayDeclared2", Boolean::class.javaObjectType),
            )
        }

        val nonDuplicatedResults = filterDuplicates(results)

        return DatabaseResult(nonDuplicatedResults, nonDuplicatedResults.size)
    }

    private fun filterDuplicates(matchResults: List<MatchDetails>): MutableList<MatchDetails> {
        val internationalMatches = matchResults.filter {
            it.matchType == "t" || it.matchType == "wt" || it.matchType == "a" || it.matchType == "wa" || it.matchType == "itt" || it.matchType == "witt"
        }

        val tempMatchResults = matchResults.toMutableList()
        internationalMatches.forEach { match ->
            matchResults.forEach { otherMatch ->
                if(match.homeTeam == otherMatch.homeTeam && match.awayTeam == otherMatch.awayTeam && match.resultString == otherMatch.resultString && match.matchType != otherMatch.matchType) {
                    tempMatchResults.remove(otherMatch)
                }
            }
        }

        return tempMatchResults
    }


    fun createMainCte(matchEndDate: LocalDate): SelectConditionStep<Record15<String?, String?, String?, java.time.LocalDate?, java.time.LocalDate?, String?, String?, String?, String?, Int?, Int?, String?, Byte?, Int?, Int?>?> {
        val cte = select(
            MATCHES.HOMETEAMNAME,
            MATCHES.AWAYTEAMNAME,
            MATCHES.RESULTSTRING,
            MATCHES.MATCHSTARTDATE,
            MATCHES.MATCHENDDATE,
            MATCHES.TOURNAMENT,
            MATCHES.MATCHDESIGNATOR,
            MATCHES.DURATIONTYPE,
            MATCHES.MATCHTYPE,
            INNINGS.TOTAL,
            INNINGS.WICKETS,
            INNINGS.OVERS,
            INNINGS.DECLARED,
            INNINGS.INNINGSORDER,
            INNINGS.MATCHID,
        ).from(MATCHES)
            .join(INNINGS)
            .on(MATCHES.ID.eq(INNINGS.MATCHID))
            .and(INNINGS.TEAMID.eq(MATCHES.HOMETEAMID))
            .where(MATCHES.VICTORYTYPE.notIn(11))
            .and(MATCHES.MATCHENDDATE.ge(matchEndDate.toJavaLocalDate()))
            .and(INNINGS.INNINGSNUMBER.eq(1))

        return cte
    }

    fun createOtherCte(
        matchEndDate: LocalDate,
        isHomeTeamId: Boolean,
        inningsNumber: Int
    ): SelectConditionStep<Record6<Int?, Int?, String?, Byte?, Int?, Int?>?> {

        val teamId = if (isHomeTeamId) {
            MATCHES.HOMETEAMID
        } else {
            MATCHES.AWAYTEAMID
        }

        val cte = select(
            INNINGS.TOTAL,
            INNINGS.WICKETS,
            INNINGS.OVERS,
            INNINGS.DECLARED,
            INNINGS.INNINGSORDER,
            INNINGS.MATCHID,
        ).from(MATCHES)
            .join(INNINGS)
            .on(MATCHES.ID.eq(INNINGS.MATCHID))
            .and(INNINGS.TEAMID.eq(teamId))
            .where(MATCHES.VICTORYTYPE.notIn(11))
            .and(MATCHES.MATCHENDDATE.ge(matchEndDate.toJavaLocalDate()))
            .and(INNINGS.INNINGSNUMBER.eq(inningsNumber))

        return cte

    }
}