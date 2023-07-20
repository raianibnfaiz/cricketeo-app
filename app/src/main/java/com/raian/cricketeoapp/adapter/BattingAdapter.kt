package com.raian.cricketeoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.models.Lineup
import com.raian.cricketeoapp.models.Batting

class BattingAdapter(
    private val lineupList: List<Lineup>?, private val battingScoreList: List<Batting>?
) : RecyclerView.Adapter<BattingAdapter.BattingViewHolder>() {
    private var Lineup = lineupList
    private var BattingList = battingScoreList

    class BattingViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val batsmenName: TextView = itemView.findViewById(R.id.batsmenName)
        val batsmenRun: TextView = itemView.findViewById(R.id.batsmenRun)
        val playedBalls: TextView = itemView.findViewById(R.id.playedBalls)
        val scoredBoundaries: TextView = itemView.findViewById(R.id.boundaries)
        val scoredSixes: TextView = itemView.findViewById(R.id.sixes)
        val strikeRate: TextView = itemView.findViewById(R.id.strikeRate)
        val playerOutInfo: TextView = itemView.findViewById(R.id.playerOutInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattingViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.batsmen_info_layout, parent, false)
        return BattingAdapter.BattingViewHolder(root)
    }


    override fun onBindViewHolder(holder: BattingViewHolder, position: Int) {
        val currentBattingData = BattingList?.get(position)
        val player = Lineup?.filter { it.id == currentBattingData?.player_id }
        if (currentBattingData != null && !player?.isEmpty()!!) {
            holder.batsmenName.text = player?.get(0)?.fullname
            holder.batsmenRun.text = currentBattingData?.score.toString()
            holder.playedBalls.text = currentBattingData?.ball.toString()
            holder.scoredBoundaries.text = currentBattingData?.four_x.toString()
            holder.scoredSixes.text = currentBattingData?.six_x.toString()
            holder.strikeRate.text = currentBattingData?.rate.toString()
            if (currentBattingData?.bowler?.fullname != null && currentBattingData?.catchstump?.fullname != null) {
                holder.playerOutInfo.text =
                    "c ${currentBattingData?.catchstump?.fullname} b ${currentBattingData?.bowler?.fullname}"
            }else if(currentBattingData.bowler?.fullname != null && currentBattingData?.catchstump?.fullname == null){
                holder.playerOutInfo.text = "b ${currentBattingData?.bowler?.fullname}"
            }
            else {
                holder.playerOutInfo.text = "not out"
            }
        }

    }

    override fun getItemCount(): Int {
        return BattingList?.size ?: 0
    }
}
