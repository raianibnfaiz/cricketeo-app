package com.raian.cricketeoapp.models

data class Squad(
    val code: String,
    val country_id: Int,
    val id: Int,
    val image_path: String,
    val name: String,
    val national_team: Boolean,
    val resource: String,
    val squad: List<SquadPlayerDetails>,
    val updated_at: String
)