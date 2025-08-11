package com.knowledgespike.feature.frontpage.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchDetails(
    val homeTeam: String,
    val awayTeam: String,
    val resultString: String,
    val durationType: String,
    val tournament: String,
    val matchType: String,
    val homeTotal1: Int?,
    val homeWickets1: Int?,
    val homeOvers1: String?,
    val homeDeclared1: Boolean?,
    val awayTotal1: Int?,
    val awayWickets1: Int?,
    val awayOvers1: String?,
    val awayDeclared1: Boolean?,
    val homeTotal2: Int?,
    val homeWickets2: Int?,
    val homeOvers2: String?,
    val homeDeclared2: Boolean?,
    val awayTotal2: Int?,
    val awayWickets2: Int?,
    val awayOvers2: String?,
    val awayDeclared2: Boolean?,
)
