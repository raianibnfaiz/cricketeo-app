package com.raian.cricketeoapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Visitorteam(
    val code: String,
    val country_id: Int,
    val id: Int,
    val image_path: String,
    val name: String,
    val national_team: Boolean,
    val resource: String,
    val updated_at: String
) : Parcelable