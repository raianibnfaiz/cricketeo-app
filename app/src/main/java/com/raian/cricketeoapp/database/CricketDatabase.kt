package com.raian.cricketeoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raian.cricketeoapp.converter.LocalteamConverter
import com.raian.cricketeoapp.converter.VisitorTeamConverter
import com.raian.cricketeoapp.models.Fixture
import com.raian.cricketeoapp.models.SquadPlayerDetails
import com.raian.cricketeoapp.models.Team
import com.raian.cricketeoapp.models.countries.Country

@Database(entities = [Team::class, Fixture::class, SquadPlayerDetails::class,Country::class], version = 19, exportSchema = false)
@TypeConverters(VisitorTeamConverter::class,LocalteamConverter::class)
abstract class CricketDatabase: RoomDatabase()  {
        abstract fun cricketDao(): CricketDao


        companion object {

            @Volatile
            private var INSTANCE: CricketDatabase? = null
            fun getDatabase(context: Context): CricketDatabase? {
                val temInstance = INSTANCE
                if (temInstance != null) {
                    return temInstance
                }
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CricketDatabase::class.java,
                        "application_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                    return instance
                }
            }
        }

}