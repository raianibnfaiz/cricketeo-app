package com.raian.cricketeoapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.models.Bowling
import com.raian.cricketeoapp.models.Lineup

class BowlingAdapter(private val lineupList: List<Lineup>?, private val bowlingInfoList: List<Bowling>?
)
    :RecyclerView.Adapter<BowlingAdapter.BowlingViewHolder>() {
        private var Lineup = lineupList
        private var BowlingList = bowlingInfoList

        class BowlingViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

            val bowlerName: TextView = itemView.findViewById(R.id.batsmenName)
            val oversBowled: TextView = itemView.findViewById(R.id.batsmenRun)
            val maiden: TextView = itemView.findViewById(R.id.playedBalls)
            val costRuns: TextView = itemView.findViewById(R.id.boundaries)
            val getWickets: TextView = itemView.findViewById(R.id.sixes)
            val economyRate: TextView = itemView.findViewById(R.id.strikeRate)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BowlingViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.bowler_info_layout, parent, false)
        return BowlingAdapter.BowlingViewHolder(root)
    }

    override fun getItemCount(): Int {
        return bowlingInfoList?.size ?: 0
    }

    override fun onBindViewHolder(holder: BowlingViewHolder, position: Int) {
        val currentBowlingData = BowlingList?.get(position)
        val player= Lineup?.filter {it.id == currentBowlingData?.player_id }
        Log.d("bowling error", "onBindViewHolder: $player")
        if (currentBowlingData != null && !player?.isEmpty()!!) {
            holder.bowlerName.text = player?.get(0)?.fullname
            holder.oversBowled.text= currentBowlingData?.overs.toString()
            holder.maiden.text = currentBowlingData?.medians.toString()
            holder.costRuns.text = currentBowlingData?.runs.toString()
            holder.getWickets.text= currentBowlingData?.wickets.toString()
            holder.economyRate.text= currentBowlingData?.rate.toString()

        }
    }

}