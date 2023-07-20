package com.raian.cricketeoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raian.cricketeoapp.models.Fixture
import com.raian.cricketeoapp.models.SquadPlayerDetails
import com.raian.cricketeoapp.models.Team
import com.raian.cricketeoapp.models.countries.Country
import retrofit2.http.Path


@Dao
interface CricketDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTeam(teams: List<Team>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayer(ranking: List<SquadPlayerDetails>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountries(ranking: List<Country>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFixtures(fixtures: List<Fixture>)


    @Query("SELECT * FROM teams")
    fun getTeam(): LiveData<List<Team>>

    @Query("SELECT * FROM teams WHERE id IN (1,10,38,42,43,46,39,36,37,40)")
    fun getTeamListContainsSquad(): LiveData<List<Team>>

    @Query("SELECT * FROM players")
    fun getPlayers(): LiveData<List<SquadPlayerDetails>>

    @Query("SELECT name FROM countries WHERE id = :id")
    fun getCountryName(id: Int): LiveData<String>

    @Query("SELECT COUNT(*) FROM players")
    fun getPlayerCount(): Int

    @Query("SELECT * FROM fixture where status= 'NS' ORDER BY starting_at ASC")
    fun getUpcomingFixtures(): LiveData<List<Fixture>>

    @Query("SELECT * FROM fixture where status= 'Finished' ORDER BY starting_at ASC")
    fun getRecentMatches(): LiveData<List<Fixture>>
}