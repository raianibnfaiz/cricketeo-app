package com.raian.cricketeoapp.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.raian.cricketeoapp.database.CricketDao
import com.raian.cricketeoapp.models.Fixture
import com.raian.cricketeoapp.models.PlayerCareer.PlayerCareerStats
import com.raian.cricketeoapp.models.SquadPlayerDetails
import com.raian.cricketeoapp.models.Team
import com.raian.cricketeoapp.models.countries.Country
import com.raian.cricketeoapp.models.fixture.FixtureDetail
import com.raian.cricketeoapp.models.league.League
import com.raian.cricketeoapp.models.leagueMatch.LeagueMatch
import com.raian.cricketeoapp.models.livescores.LiveScore
import com.raian.cricketeoapp.models.matchesOfTeam.MatchesOfTeam
import com.raian.cricketeoapp.models.ranking.RankingTeam
import com.raian.cricketeoapp.network.CricketApi
import com.raian.cricketeoapp.network.NetworkUtil
import com.raian.cricketeoapp.network.NoInternetException
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CricketRepository(private val cricketDao: CricketDao) {
    val networkUtil = NetworkUtil()
    fun checkConnection(){
        if(!networkUtil.isConnected()){
            throw NoInternetException("No Internet")
        }
    }
    suspend fun insertTeam(team: List<Team>) {
        cricketDao.insertTeam(team)
    }

    suspend fun insertFixtures(fixture: List<Fixture>) {
        cricketDao.insertFixtures(fixture)
    }

//    suspend fun insertTeamRankings(teamRankings: List<com.raian.cricketeoapp.models.ranking.Team>){
//        cricketDao.insertTeamRanking(teamRankings)
//    }

    suspend fun insertPlayer(player: List<SquadPlayerDetails>) {
        cricketDao.insertPlayer(player)
    }

    suspend fun insertCountries(country: List<Country>) {
        cricketDao.insertCountries(country)
    }


    fun getTeam(): LiveData<List<Team>> {
        return cricketDao.getTeam()
    }
    fun getTeamListContainsSquad(): LiveData<List<Team>> {
        return cricketDao.getTeamListContainsSquad()
    }

    suspend fun getTeamList(): List<Team> {
        checkConnection()
        return CricketApi.retrofitService.getTeams().data
    }

    fun getPlayers(): LiveData<List<SquadPlayerDetails>> {
        return cricketDao.getPlayers()
    }
    fun getCountryName(id:Int): LiveData<String> {
        return cricketDao.getCountryName(id)
    }
    fun getUpcomingFixtures(): LiveData<List<Fixture>> {
        return cricketDao.getUpcomingFixtures()
    }

    fun getRecentMatches(): LiveData<List<Fixture>> {
        return cricketDao.getRecentMatches()
    }

    suspend fun refreshTeams() {
        checkConnection()
        val response = CricketApi.retrofitService.getTeams()
        insertTeam(response.data)
    }

    suspend fun refreshCountries() {
        checkConnection()
        val response = CricketApi.retrofitService.getCountries()
        insertCountries(response.data)
    }

    suspend fun refreshPlayers() {
        if(cricketDao.getPlayerCount()>0){
            return
        }
        checkConnection()
        val teams = getTeamList()
        Log.d("players!", "refreshPlayers: $teams")
        for (i in teams) {
            if (i.national_team) {
                val teamId = i.id
                delay(5000L)
                val response = CricketApi.retrofitService.getTeamSquad(teamId)
                insertPlayer(response.data.squad)
            }
        }
    }

    suspend fun getTeamRanking(type: String, gender: String): List<RankingTeam>? {
        checkConnection()
        val response = CricketApi.retrofitService.getTeamRanking(type, gender)
        //accessing index 0 because server sends both men and women, men comes from index 0
        Log.d("TEST Rank", "getTeamRanking: ${response.data.get(0).team}")
        return response.data[0].team
    }
    suspend fun getLeagueMatches(leagueId:Int):List<LeagueMatch>?{
        checkConnection()
        val response = CricketApi.retrofitService.getLeagueMatchesById(leagueId)
        //accessing index 0 because server sends both men and women, men comes from index 0
        Log.d("league", "getLeagueMatches: ${response.data}")
        return response.data
    }
    suspend fun getAllTeamMatches(teamId:Int):List<MatchesOfTeam>?{
        checkConnection()
        val response = CricketApi.retrofitService.getMatchesByTeamId(teamId)
        //accessing index 0 because server sends both men and women, men comes from index 0
        Log.d("all team matches", "getTeamMatches: ${response.data}")
        return response.data
    }
     suspend fun getPlayerCareer(playerId:Int): PlayerCareerStats {
        //checkConnection()
        val response = CricketApi.retrofitService.getPlayerCareer(playerId)
        Log.d("Career", "getPlayerCareer: ${response.data.fullname}")
        return response.data
    }
    suspend fun getLeagues():List<League>{
        checkConnection()
        val response = CricketApi.retrofitService.getLeagues()
        Log.d("League", "get leagues : ${response.data}")
        return response.data
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getFixturesMatches(): List<FixtureDetail> {
        checkConnection()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = LocalDateTime.now().format(formatter)
        val end = LocalDateTime.now().plusMonths(2).format(formatter)
        val dateRange = "${start},${end}"
        val response = CricketApi.retrofitService.getFixturesMatches(dateRange)
        Log.d("Fixture", "getUpcoming Matches: ${response.data} ${dateRange}")
        val filteredMatches = ArrayList<FixtureDetail>()
        for (match in response.data) {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val date = ZonedDateTime.parse(match.starting_at, formatter)

            if (!date.isBefore(ZonedDateTime.now())) {
                filteredMatches.add(match)
            }
        }
        return filteredMatches
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getRecentFixturesMatches(): List<FixtureDetail> {
        checkConnection()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val end = LocalDateTime.now().format(formatter)
        val start = LocalDateTime.now().minusMonths(2).format(formatter)
        val dateRange = "${start},${end}"
        val response = CricketApi.retrofitService.getRecentFixturesMatches(dateRange)
        Log.d("Fixture", "getRecent matches: ${response.data}")
        return response.data
    }
    suspend fun getLiveScore(): List<LiveScore> {
        checkConnection()
        val response = CricketApi.retrofitService.getLiveScore()
        Log.d("live", "get live score: ${response.data}")
        return response.data
    }

}
