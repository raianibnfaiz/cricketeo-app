package com.raian.cricketeoapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class SquadPlayerDetails (
    val battingstyle: String?,
    val bowlingstyle: String?,
    val country_id: Int?,
    val dateofbirth: String?,
    val firstname: String?,
    val fullname: String?,
    val gender: String?,
    @PrimaryKey
    val id: Int?,
    val image_path: String?,
    val lastname: String?,
    //val position: Position?,
    val resource: String?,
    //val squad: SquadXX?,
    val updated_at: String?
)