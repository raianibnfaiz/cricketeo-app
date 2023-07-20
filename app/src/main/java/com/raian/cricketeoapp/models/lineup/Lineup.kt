package com.raian.cricketeoapp.models.lineup

data class Lineup(
    val captain: Boolean,
    val substitution: Boolean,
    val team_id: Int,
    val wicketkeeper: Boolean
)