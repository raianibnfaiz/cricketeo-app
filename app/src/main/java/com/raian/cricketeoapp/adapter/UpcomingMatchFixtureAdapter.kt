package com.raian.cricketeoapp.adapter

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.fragment.CricketeoHomeFragmentDirections
import com.raian.cricketeoapp.models.fixture.FixtureDetail
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class UpcomingMatchFixtureAdapter(
    private val context: Context, val viewModel: CricketViewModel,
    private val arrayList: List<FixtureDetail>?

) : RecyclerView.Adapter<UpcomingMatchFixtureAdapter.MatchFixtureViewHolder>() {
    private var theTeamList = arrayList
    private var timerAdded =ArrayList<Boolean>()


    class MatchFixtureViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val itemTeam1: TextView = itemView.findViewById(R.id.localTeamRun)
        val itemTeam2: TextView = itemView.findViewById(R.id.visitorTeamRun)
        val itemDate: TextView = itemView.findViewById(R.id.result)
        val itemPicture1: ImageView = itemView.findViewById(R.id.localTeamImage)
        val itemPicture2: ImageView = itemView.findViewById(R.id.VisitorTeamImage)
        val itemBellIcon: ImageFilterButton = itemView.findViewById(R.id.bellIcon)
        val cardView: CardView = itemView.findViewById(R.id.cardId)
        val itemRound: TextView = itemView.findViewById(R.id.venue)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchFixtureViewHolder {
        val root =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fixtures_item_layout, parent, false)
        return UpcomingMatchFixtureAdapter.MatchFixtureViewHolder(root)
    }

    override fun getItemCount(): Int {
        return theTeamList?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MatchFixtureViewHolder, position: Int) {
        val currentData = theTeamList?.get(position)
        if (currentData != null){
            if (!TextUtils.isEmpty(currentData.localteam?.name)) {
                holder.itemTeam1.text = currentData.localteam?.name
            } else {
                holder.itemTeam1.text = "Title Missing!"
            }
            if (!TextUtils.isEmpty(currentData.visitorteam?.name)) {
                holder.itemTeam2.text = currentData.visitorteam?.name
            } else {
                holder.itemTeam2.text = "Title Missing!"
            }

            if (currentData.starting_at!=null) {
                val date = ZonedDateTime.parse(currentData.starting_at, DateTimeFormatter.ISO_DATE_TIME)
                val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
                val matchDate = date.format(formatterDate)
                val matchTime = date.format(formatterTime)
                holder.itemDate.text =
                    "Game will start on ${matchDate} at ${matchTime}"

            } else {

                holder.itemDate.text = "date missing"

            }

            if (!TextUtils.isEmpty(currentData.league?.name)) {
                holder.itemRound.text = currentData.league?.name
            } else {
                holder.itemRound.text = "Match Round Missing!"
            }

            if (!TextUtils.isEmpty(currentData.localteam?.image_path)) {
                if (currentData != null) {
                    Picasso.get()
                        .load(currentData.localteam?.image_path)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .into(holder.itemPicture1)
                }
            } else {
                Picasso.get()
                    .load(R.drawable.ic_connection_error)
                    .fit()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .centerCrop(1)
                    .centerCrop()
                    .into(holder.itemPicture1)
            }

            if (!TextUtils.isEmpty(currentData.visitorteam?.image_path)) {
                Picasso.get()
                    .load(currentData.visitorteam?.image_path)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .into(holder.itemPicture2)
            } else {
                Picasso.get()
                    .load(R.drawable.ic_connection_error)
                    .fit()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .centerCrop(1)
                    .centerCrop()
                    .into(holder.itemPicture2)
            }

            if (currentData.status != "Finished") {

                holder.itemBellIcon.setOnClickListener {
                    currentData.localteam?.name?.let { it1 ->
                        currentData.visitorteam?.name?.let { it2 ->
                            viewModel.subscribe(
                                context,
                                it1, it2, currentData.id, currentData.round, currentData.starting_at
                            )
                        }
                    }
                }
            }

            if (currentData.status == "Finished") {

                holder.cardView.setOnClickListener {
                    Toast.makeText(context, "${currentData.id}", Toast.LENGTH_SHORT).show()
                    val action =
                        CricketeoHomeFragmentDirections.actionCricketeoHomeFragmentToScorecardHostFragment(
                            currentData.id!!
                        )
                    holder.itemView.findNavController().navigate(action)
                }
            }
        }
    }
}