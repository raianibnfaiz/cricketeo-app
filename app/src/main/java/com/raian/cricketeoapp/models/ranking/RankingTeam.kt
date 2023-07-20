package com.raian.cricketeoapp.models.ranking

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class RankingTeam(
    val code: String?,
    val country_id: Int?,
    //PrimaryKey
    val id: Int?,
    val image_path: String?,
    val name: String?,
    val national_team: Boolean?,
    val position: Int?,
    //val ranking: Ranking?,
    val resource: String?,
    val updated_at: String?
)
