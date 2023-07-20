package com.raian.cricketeoapp.models.ranking


data class RankingDataSource(
    val gender: String?,
    val points: Int?,
    val position: Int?,
    val rating: Int?,
    val resource: String?,
    val team: List<RankingTeam>?,
    val type: String?,
    val updated_at: String
)
//{
//    constructor():this(
//        0,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null
//    )
//}
