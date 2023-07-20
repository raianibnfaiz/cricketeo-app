package com.raian.cricketeoapp.viewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.*
import com.raian.cricketeoapp.models.PlayerCareer.PlayerCareerStats
import com.raian.cricketeoapp.models.fixture.FixtureDetail
import com.raian.cricketeoapp.models.league.League
import com.raian.cricketeoapp.models.leagueMatch.LeagueMatch
import com.raian.cricketeoapp.models.livescores.LiveScore
import com.raian.cricketeoapp.models.matchesOfTeam.MatchesOfTeam
import com.raian.cricketeoapp.models.ranking.RankingTeam
import com.raian.cricketeoapp.network.CricketApi
import com.raian.cricketeoapp.network.NoInternetException
import com.raian.cricketeoapp.receivers.AlarmReceiver
import com.raian.cricketeoapp.repository.CricketRepository
import kotlinx.coroutines.*
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CricketViewModel(application: Application) : AndroidViewModel(application) {
    private val _upcomingFixture = MutableLiveData<List<FixtureDetail>>()
    val upcomingFixture:LiveData<List<FixtureDetail>> =_upcomingFixture
    private val _recentFixture = MutableLiveData<List<FixtureDetail>>()
    val recentFixture:LiveData<List<FixtureDetail>> =_recentFixture
    private var _squadWrapper = MutableLiveData<List<SquadWrapper>>()
    val squadWrapper: LiveData<List<SquadWrapper>> = _squadWrapper
    lateinit var repository: CricketRepository
    val readAllTeam: LiveData<List<Team>>
    val readAllTeamContainsSquad: LiveData<List<Team>>
    val readAllPlayers: LiveData<List<SquadPlayerDetails>>
    private val _total = MutableLiveData<String>()
    val total: LiveData<String> = _total
    private var _readPlayerCareer= MutableLiveData<PlayerCareerStats>()
    val readPlayerCareer:LiveData<PlayerCareerStats> = _readPlayerCareer
    val readAllUpcomingFixtures: LiveData<List<Fixture>>
    val readAllRecentMatches: LiveData<List<Fixture>>

    init {
        Log.e("Error", "init")
        val applicationDao = CricketDatabase.getDatabase(application)?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        readAllTeam = repository.getTeam()
        readAllTeamContainsSquad = repository.getTeamListContainsSquad()
        readAllUpcomingFixtures = repository.getUpcomingFixtures()
        readAllRecentMatches = repository.getRecentMatches()
        readAllPlayers=repository.getPlayers()
        getPlayers()
        getCurrentPlayerList()
        getCountries()
        getCountryName(155043)

    }

    private fun getCurrentPlayerList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlayers()
        }
    }

    private fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.refreshCountries()
            } catch (e:NoInternetException){
                Log.d("error checking", "no internet: $e")
            } catch (e:java.lang.Exception){
                Log.d("error checking", "other unexpected error: $e")
            }

        }
    }
    suspend fun getTeamRanking(type:String,gender:String):Result<List<RankingTeam>?>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val ranking = repository.getTeamRanking(type,gender)
                Result.success(ranking)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }.await()
    }
    suspend fun getLeagueMatches(leagueId:Int):Result<List<LeagueMatch>?>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val leagueMatches = repository.getLeagueMatches(leagueId)
                Result.success(leagueMatches)
            } catch (e: Exception) {
                Log.d("leagueMatch error", "leagueMatch: $e")
                Result.failure(e)
            }
        }.await()
    }
    suspend fun getAllTeamMatches(teamId:Int):Result<List<MatchesOfTeam>?>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val leagueMatches = repository.getAllTeamMatches(teamId)
                Result.success(leagueMatches)
            } catch (e: Exception) {
                Log.d("leagueMatch error", "leagueMatch: $e")
                Result.failure(e)
            }
        }.await()
    }
     fun getPlayerCareer(playerId:Int){
         /*viewModelScope.launch {
             repository.getPlayerCareer(playerId)
         }*/
         viewModelScope.launch {
             try {
                 _readPlayerCareer.value= repository.getPlayerCareer(playerId)
                 //Log.d("fixture", "getFixturesMatches: $fixture")

             } catch (e: Exception) {
                 Log.d("fixture", "getFixturesMatches: $e")

             }
         }

    }


    fun getCountryName(id:Int): LiveData<String> {
        Log.d("check", "getCountryName: ${repository.getCountryName(id).value}")
        return repository.getCountryName(id)
    }

     @RequiresApi(Build.VERSION_CODES.O)
     suspend fun getUpcomingFixturesMatches(): Result<List<FixtureDetail>> {
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val fixture = repository.getFixturesMatches()
                //Log.d("fixture", "getFixturesMatches: $fixture")
                Result.success(fixture)
            } catch (e: Exception) {
                Log.d("fixture", "getFixturesMatches: $e")
                Result.failure(e)

            }
        }.await()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getRecentFixturesMatches(): Result<List<FixtureDetail>>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val fixture = repository.getRecentFixturesMatches()
                Result.success(fixture)
            }catch (e:Exception){
                Result.failure(e)
            }
        }.await()
    }
    suspend fun getLiveScore(): Result<List<LiveScore>>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val liveScore = repository.getLiveScore()
                Result.success(liveScore)
            }catch (e:Exception){
                Result.failure(e)
            }
        }.await()
    }
    suspend fun getLeagues():Result<List<League>>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val leagues = repository.getLeagues()
                Result.success(leagues)
            }catch (e:Exception){
                Result.failure(e)
            }
        }.await()
    }

    suspend fun getSquad(teamID: Int): Result<Squad> {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                val squadWrapper = CricketApi.retrofitService.getTeamSquad(teamID)
                Result.success(squadWrapper.data)
            } catch (e: Exception) {
                Log.d("Error Squad", "${e} ")
                Result.failure(e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun subscribe(
        context: Context,
        localTeam: String,
        visitorTeam: String,
        id: Int?,
        round: String?,
        startingAt: String?
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        // Used to differentiate PendingIntents
        intent.setData(Uri.parse("${localTeam}/${visitorTeam}/${id}"))
        intent.putExtra("localTeam", localTeam)
        intent.putExtra("visitorTeam", visitorTeam)
        intent.putExtra("matchId", id)
        intent.putExtra("matchRound", round)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val date = ZonedDateTime.parse(startingAt, formatter)
        val duration = Duration.between(ZonedDateTime.now(date.zone), date)
        val millisInFuture = duration.toMillis()
        alarmManager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + millisInFuture-10*60*1000,
        //System.currentTimeMillis() + 10*1000,
            pendingIntent
        )
        Toast.makeText(context, "Subscribed for notification", Toast.LENGTH_SHORT).show()
    }

    private fun getPlayers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshPlayers()
        }
    }

    suspend fun getScoreboard(id: Int): Result<com.raian.cricketeoapp.models.MatchResult> {
        return withContext(viewModelScope.coroutineContext) {
            try {
                val result = CricketApi.retrofitService.getRecentMatchScoreboards(id)
                Log.d("Scoreboard", "debug: ${result.data}")
                (Result.success(result.data))

            } catch (e: Exception) {
                Log.d("score error", "getScoreboard: $e")
                Result.failure(e)

            }
        }

    }
}

