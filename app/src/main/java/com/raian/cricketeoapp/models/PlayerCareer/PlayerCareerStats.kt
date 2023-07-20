package com.raian.cricketeoapp.models.PlayerCareer

data class PlayerCareerStats(
    val battingstyle: String?,
    val bowlingstyle: Any?,
    val career: List<Career>?,
    val country_id: Int?,
    val dateofbirth: String?,
    val firstname: String?,
    val fullname: String?,
    val gender: String?,
    val id: Int?,
    val image_path: String?,
    val lastname: String?,
    val position: Position?,
    val resource: String?,
    val updated_at: String?
)