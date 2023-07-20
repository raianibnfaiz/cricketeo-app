package com.raian.cricketeoapp.converter

import androidx.room.TypeConverter
import com.raian.cricketeoapp.models.Visitorteam
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class VisitorTeamConverter {
    @TypeConverter
    fun fromVisitorTeam(value: Visitorteam): String ?{
        if(value != null){
            return "${value.code}=${value.country_id}=${value.id}=${value.image_path}=${value.name}=${value.national_team}=${value.resource}==${value.updated_at}"
        }
        return null
    }

    @TypeConverter
    fun toVisitorTeam(output: String): Visitorteam? {
        if(output!= null){
            val value = output.split("=")
            return Visitorteam(value[0],value[1].toInt(),value[2].toInt(),value[3],value[4],value[5].toBoolean(),value[6],value[7])
        }
        return null
    }
}
