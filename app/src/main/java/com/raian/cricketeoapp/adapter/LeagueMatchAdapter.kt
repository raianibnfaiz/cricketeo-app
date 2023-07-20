package com.raian.cricketeoapp.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.fragment.CricketeoHomeFragmentDirections
import com.raian.cricketeoapp.fragment.League.LeagueMatchesFragmentDirections
import com.raian.cricketeoapp.models.leagueMatch.LeagueMatch
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso

class LeagueMatchAdapter(
    private val context: Context, val viewModel: CricketViewModel,
    private val arrayList: List<LeagueMatch>?
) : RecyclerView.Adapter<LeagueMatchAdapter.LeagueMatchViewHolder>() {
    private var theTeamList = arrayList


    class LeagueMatchViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val localTeamName: TextView = itemView.findViewById(R.id.nameLocalTeam)
        val localTeamRun: TextView = itemView.findViewById(R.id.localTeamRun)
        val visitorTeamName: TextView = itemView.findViewById(R.id.nameVisitorTeam)
        val visitorTeamRun: TextView = itemView.findViewById(R.id.visitorTeamRun)
        val matchNote: TextView = itemView.findViewById(R.id.result)
        val localTeamImage: ImageView = itemView.findViewById(R.id.localTeamImage)
        val visitorTeamImage: ImageView = itemView.findViewById(R.id.VisitorTeamImage)
        val cardView: CardView = itemView.findViewById(R.id.cardId)
        val itemVenue: TextView = itemView.findViewById(R.id.venue)
        val itemRound: TextView = itemView.findViewById(R.id.round)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueMatchViewHolder {
        val root =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.live_match_item_layout, parent, false)
        return LeagueMatchAdapter.LeagueMatchViewHolder(root)
    }

    override fun getItemCount(): Int {
        return theTeamList?.size ?: 0
    }

    override fun onBindViewHolder(holder: LeagueMatchViewHolder, position: Int) {
        val currentData = theTeamList?.get(position)

        var scoreBoardMatch = currentData?.scoreboards
        if (scoreBoardMatch != null) {
            for (i in scoreBoardMatch) {
                Log.d("runs", "onBindViewHolder: $i")
            }
        }
        var localTeamRunsScored =
            scoreBoardMatch?.filter { it.team_id == currentData?.localteam_id && it.type == "total" }
        val visitorTeamRunsScored =
            scoreBoardMatch?.filter { it.team_id == currentData?.visitorteam_id && it.type == "total" }
        if (currentData != null) {
            var totalLocalTeam = ""
            var wicketsLocalTeam = ""
            var oversLocalTeam = ""
            var totalVisitorTeam = ""
            var wicketsVisitorTeam = ""
            var oversVisitorTeam = ""
            if (!localTeamRunsScored?.isEmpty()!!) {
                totalLocalTeam = localTeamRunsScored?.get(0)?.total.toString()
                wicketsLocalTeam = localTeamRunsScored?.get(0)?.wickets.toString()
                oversLocalTeam = localTeamRunsScored?.get(0)?.overs.toString()
                holder.localTeamRun.text =
                    "${totalLocalTeam}/${wicketsLocalTeam}(${oversLocalTeam})"
            }else{
                holder.localTeamRun.text ="score missing"
            }


            if (!visitorTeamRunsScored?.isEmpty()!!) {
                totalVisitorTeam = visitorTeamRunsScored?.get(0)?.total.toString()
                wicketsVisitorTeam = visitorTeamRunsScored?.get(0)?.wickets.toString()
                oversVisitorTeam = visitorTeamRunsScored?.get(0)?.overs.toString()
                holder.visitorTeamRun.text =
                    "${totalVisitorTeam}/${wicketsVisitorTeam}(${oversVisitorTeam})"
            }else{
                holder.localTeamRun.text ="score missing"
            }


            if (!TextUtils.isEmpty(currentData.localteam?.name)) {
                holder.localTeamName.text = currentData.localteam?.name
            } else {
                holder.localTeamName.text = "Title Missing!"
            }

            if (!TextUtils.isEmpty(currentData.visitorteam?.name)) {
                holder.visitorTeamName.text = currentData.visitorteam?.name
            } else {
                holder.visitorTeamName.text = "Title Missing!"
            }
            holder.matchNote.text = currentData.note
            holder.itemVenue.text = currentData.venue?.name
            holder.itemRound.text = currentData.round
            holder.cardView.setOnClickListener {
                val action =
                    CricketeoHomeFragmentDirections.actionCricketeoHomeFragmentToScorecardHostFragment(
                        currentData.id!!
                    )
                holder.itemView.findNavController().navigate(action)
            }

            if (!TextUtils.isEmpty(currentData.localteam?.image_path)) {
                if (currentData != null) {
                    Picasso.get()
                        .load(currentData.localteam?.image_path)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .into(holder.localTeamImage)
                }
            } else {
                Picasso.get()
                    .load(R.drawable.ic_connection_error)
                    .fit()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .centerCrop(1)
                    .centerCrop()
                    .into(holder.localTeamImage)
            }
            if (!TextUtils.isEmpty(currentData.visitorteam?.image_path)) {
                Picasso.get()
                    .load(currentData.visitorteam?.image_path)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .into(holder.visitorTeamImage)
            } else {
                Picasso.get()
                    .load(R.drawable.ic_connection_error)
                    .fit()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .centerCrop(1)
                    .centerCrop()
                    .into(holder.visitorTeamImage)
            }

        }
        if (currentData != null) {
            if (currentData.status == "Finished") {
                holder.cardView.setOnClickListener {
                    val action = currentData?.id?.let { it1 ->
                        LeagueMatchesFragmentDirections.actionLeagueMatchesFragmentToScorecardHostFragment(
                            it1
                        )
                    }
                    if (action != null) {
                        holder.itemView.findNavController().navigate(action)
                    }
                }
            }
        }

    }

}