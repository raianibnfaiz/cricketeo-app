package com.raian.cricketeoapp.models.countries

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Country(
    val continent_id: Int?,
    @PrimaryKey
    val id: Int,
    val image_path: String?,
    val name: String?,
    val resource: String?,
    val updated_at: String?
)