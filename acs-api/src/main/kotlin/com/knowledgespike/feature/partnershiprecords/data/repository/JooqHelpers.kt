package com.knowledgespike.feature.partnershiprecords.data.repository


import com.knowledgespike.db.tables.Extramatchdetails.Companion.EXTRAMATCHDETAILS
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.db.tables.references.*
import com.knowledgespike.feature.recordsearch.domain.model.ValidatedSearchParameters
import org.jooq.Condition
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.TableField
import org.jooq.impl.DSL.*
import org.jooq.impl.UpdatableRecordImpl

fun initialSelect(searchParameters: ValidatedSearchParameters, searchCondition: Condition,
                  partitionBy: List<TableField<out UpdatableRecordImpl<*>, out Comparable<*>?>>
): SelectConditionStep<Record> {
    return select(
        MATCHES.CAID,
        PARTNERSHIPS.PARTNERSHIP.`as`("runs"),
        PARTNERSHIPS.INNINGSORDER.`as`("innings"),
        PARTNERSHIPS.WICKET,
        PARTNERSHIPS.PREVIOUSWICKET,
        PARTNERSHIPS.PREVIOUSSCORE,
        PARTNERSHIPS.PLAYERIDS,
        PARTNERSHIPS.PLAYERNAMES,
        PARTNERSHIPS.CURRENTSCORE,
        PARTNERSHIPS.FIFTY,
        PARTNERSHIPS.HUNDRED,
        PARTNERSHIPS.UNBROKEN.`as`("Unbroken1"),
        PARTNERSHIPS.UNBROKEN.`as`("Unbroken2"),
        PARTNERSHIPS.MULTIPLE,
        PARTNERSHIPS.PARTIAL,
        PARTNERSHIPS.SYNTHETICPARTNERSHIP,
        field("T.Name").`as`("teams"),
        field("O.Name").`as`("opponents"),
        GROUNDS.KNOWNAS,
        PLAYERS.FULLNAME.`as`("player1"),
        PLAYERS.ID.`as`("player1Id"),
        lead(PLAYERS.FULLNAME).over()
            .partitionBy(partitionBy)
            .orderBy(partitionBy)
            .`as`("player2"),
        lead(PLAYERS.ID).over()
            .partitionBy(partitionBy)
            .orderBy(partitionBy)
            .`as`("player2Id"),
        MATCHES.MATCHSTARTDATEASOFFSET,
        MATCHES.MATCHSTARTDATE,
        MATCHES.MATCHTITLE,
        MATCHES.RESULTSTRING,
        rowNumber().over()
            .partitionBy(partitionBy)
            .orderBy(partitionBy)
            .`as`("rn"),
    )
        .from(PARTNERSHIPS)
        .join(MATCHES).on(PARTNERSHIPS.MATCHID.eq(MATCHES.ID))
        .join(EXTRAMATCHDETAILS).on(PARTNERSHIPS.MATCHID.eq(EXTRAMATCHDETAILS.MATCHID))
        .and(EXTRAMATCHDETAILS.TEAMID.eq(PARTNERSHIPS.TEAMID))
        .leftJoin(PARTNERSHIPSPLAYERS).on(PARTNERSHIPS.ID.eq(PARTNERSHIPSPLAYERS.PARTNERSHIPID))
        .leftJoin(PLAYERS).on(PARTNERSHIPSPLAYERS.PLAYERID.eq(PLAYERS.ID))
        .join(TEAMS.`as`("T")).on(PARTNERSHIPS.TEAMID.eq(field("T.ID", Int::class.java)))
        .join(TEAMS.`as`("O")).on(PARTNERSHIPS.OPPONENTSID.eq(field("O.ID", Int::class.java)))
        .join(GROUNDS).on(GROUNDS.ID.eq(MATCHES.LOCATIONID))
        .where(PARTNERSHIPS.MATCHTYPE.eq(searchParameters.matchType.value))
        .and(PARTNERSHIPS.MULTIPLE.eq(0))
        .and(
            PARTNERSHIPS.MATCHID.`in`(
                select(MATCHSUBTYPE.MATCHID).from(MATCHSUBTYPE)
                    .where(MATCHSUBTYPE.MATCHTYPE.eq(searchParameters.matchSubType.value))
            )
        )
        .and(searchCondition)
}