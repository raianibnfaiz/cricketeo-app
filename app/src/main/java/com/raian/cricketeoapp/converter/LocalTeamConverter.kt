package com.raian.cricketeoapp.converter

import androidx.room.TypeConverter
import com.raian.cricketeoapp.models.Localteam

class LocalteamConverter {
    @TypeConverter
    fun fromLocalteam(value: Localteam): String ?{
        if(value != null){
            return "${value.code}=${value.country_id}=${value.id}=${value.image_path}=${value.name}=${value.national_team}=${value.resource}==${value.updated_at}"
        }
        return null
    }

    @TypeConverter
    fun toLocalteam(output: String): Localteam? {
        if(output!= null){
            val value = output.split("=")
            return Localteam(value[0],value[1].toInt(),value[2].toInt(),value[3],value[4],value[5].toBoolean(),value[6],value[7])
        }
        return null
    }
}