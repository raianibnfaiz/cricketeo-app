package com.raian.cricketeoapp.network

import com.raian.cricketeoapp.models.Fixtures
import com.raian.cricketeoapp.models.MatchResultWrapper
import com.raian.cricketeoapp.models.PlayerCareer.PlayerCareerWrapper
import com.raian.cricketeoapp.models.SquadWrapper
import com.raian.cricketeoapp.models.Teams
import com.raian.cricketeoapp.models.countries.Countries
import com.raian.cricketeoapp.models.fixture.MatchFixtures
import com.raian.cricketeoapp.models.league.LeagueDataSource
import com.raian.cricketeoapp.models.leagueMatch.LeagueMatchWrapper
import com.raian.cricketeoapp.models.livescores.LiveScoreMatchWrapper
import com.raian.cricketeoapp.models.matchesOfTeam.MatchesOfTeamWrapper
import com.raian.cricketeoapp.models.ranking.Rankings

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val BASE_URL = "https://cricket.sportmonks.com/api/v2.0/"
const val API_KEY = "0qzN0XTU2q7rSf1XxvorzzxgRGieFINDXI20t0pxwzDcCHQX9Dv1aSofh8ud"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL)
        .build()

interface CricketApiInterface {

    @GET("teams?api_token=${API_KEY}")
    suspend fun getTeams(): Teams

    @GET("team-rankings?api_token=${API_KEY}")
    suspend fun getTeamRanking(@Query("filter[type]") type: String?,@Query("filter[gender]") gender: String?): Rankings


    @GET("countries?api_token=${API_KEY}")
    suspend fun getCountries(): Countries

    @GET("leagues?api_token=${API_KEY}")
    suspend fun getLeagues(): LeagueDataSource

    @GET("fixtures?include=scoreboards,league,manofmatch,venue,visitorteam,localteam&sort=starting_at&api_token=${API_KEY}")
    suspend fun getFixturesMatches(@Query("filter[starts_between]") dateRange:String):MatchFixtures

    @GET("fixtures?include=scoreboards,league,manofmatch,venue,visitorteam,localteam&sort=starting_at&&api_token=${API_KEY}")
    suspend fun getRecentFixturesMatches(@Query("filter[starts_between]")dateRange:String):MatchFixtures

    @GET("teams/{team_id}/squad/23?api_token=${API_KEY}")
    suspend fun getTeamSquad(@Path("team_id") teamId: Int): SquadWrapper

    @GET("fixtures/{match_id}?include=scoreboards,league,manofmatch,lineup,batting,batting.bowler,batting.catchStump,bowling,venue,visitorteam,localteam,balls&api_token=${API_KEY}")
    suspend fun getRecentMatchScoreboards(@Path("match_id") matchId: Int): MatchResultWrapper

    @GET("players/{player_id}?include=career&api_token=${API_KEY}")
    suspend fun getPlayerCareer(@Path("player_id") player_id: Int):PlayerCareerWrapper

    @GET("fixtures?include=localteam,visitorteam,scoreboards,venue&api_token=${API_KEY}")
    suspend fun getLeagueMatchesById(@Query("filter[league_id]") league_id: Int?): LeagueMatchWrapper

    @GET("fixtures?include=localteam,visitorteam,scoreboards,venue&api_token=${API_KEY}")
    suspend fun getMatchesByTeamId(@Query("filter[localteam_id]") localteam_id: Int?): MatchesOfTeamWrapper

    @GET("livescores?include=scoreboards,localteam,visitorteam,tosswon,venue&api_token=${API_KEY}")
    suspend fun getLiveScore():LiveScoreMatchWrapper
}

object CricketApi {
    val retrofitService: CricketApiInterface by lazy { retrofit.create(CricketApiInterface::class.java) }
}

