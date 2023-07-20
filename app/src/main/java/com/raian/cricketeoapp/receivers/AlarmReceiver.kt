package com.raian.cricketeoapp.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.raian.cricketeoapp.MainActivity
import com.raian.cricketeoapp.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

class AlarmReceiver : BroadcastReceiver() {
    private val TAG: String = "MyAlarmReceiver"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val localTeam= intent.getStringExtra("localTeam")
        val visitorTeam= intent.getStringExtra("visitorTeam")
        val matchId= intent.getIntExtra("matchId", 0)
        val matchRound = intent.getStringExtra("matchRound")
        Log.d(TAG, "onReceive: ${localTeam}, ${visitorTeam} ${matchRound}")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "channel1",
                "default",
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        val notification =
            NotificationCompat.Builder(context, "channel1").setContentTitle("Upcoming Match")
                .setContentText("${localTeam} vs ${visitorTeam} ${matchRound} match will start in 10 minutes")
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.baseline_people_24))
                //.setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
                //.setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.resources, R.drawable.baseline_people_24)))
                .setAutoCancel(true)
                .build()
        val tag = "${localTeam}:${visitorTeam}:${matchId}:${matchRound}"
        notificationManager.notify(tag,1, notification)
    }
}