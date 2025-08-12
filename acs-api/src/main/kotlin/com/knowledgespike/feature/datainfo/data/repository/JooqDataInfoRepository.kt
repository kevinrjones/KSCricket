package com.knowledgespike.feature.datainfo.data.repository

import com.knowledgespike.DIALECT
import com.knowledgespike.db.tables.Matches.Companion.MATCHES
import com.knowledgespike.feature.datainfo.domain.repository.DataInfoRepository
import com.knowledgespike.plugins.DataSource
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toKotlinLocalDateTime
import org.jooq.impl.DSL.using

class JooqDataInfoRepository(private val dataSource: DataSource) : DataInfoRepository {
    @OptIn(FormatStringsInDatetimeFormats::class)
    override fun getLatestDateAddedToDatabase(): String? {
        val context = using(dataSource.dataSource, DIALECT)

        // select AddedDate from Matches order by AddedDate desc limit 1;
        val result =
            context.select(MATCHES.ADDEDDATE)
                .from(MATCHES)
                .orderBy(MATCHES.ADDEDDATE.desc())
                .limit(1)
                .fetchOne()

        val dt= result?.component1()?.toKotlinLocalDateTime()


        return dt?.toString()
    }
}