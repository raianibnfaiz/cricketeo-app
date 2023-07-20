package com.raian.cricketeoapp.models.fixture

data class MatchFixtures(
    val `data`: List<FixtureDetail>,
    val links: Links?,
    val meta: Meta?
)