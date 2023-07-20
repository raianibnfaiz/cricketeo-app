package com.raian.cricketeoapp.models.matchesOfTeam

data class MatchesOfTeamWrapper(
    val `data`: List<MatchesOfTeam>,
    val links: Links,
    val meta: Meta
)