package com.raian.cricketeoapp.models.leagueMatch

data class LeagueMatchWrapper(
    val `data`: List<LeagueMatch>,
    val links: Links,
    val meta: Meta
)