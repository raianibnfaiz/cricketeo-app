package com.raian.cricketeoapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team(
    val code: String,
    val country_id: Int,
    @PrimaryKey
    val id: Int,
    val image_path: String,
    val name: String,
    val national_team: Boolean,
    val resource: String
)