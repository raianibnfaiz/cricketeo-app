/*
package com.raian.cricketeoapp.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.models.Fixture
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso

class FixturesAdapter(private val context: Context, val viewModel: CricketViewModel,
                      private val arrayList: List<Fixture>): RecyclerView.Adapter<FixturesAdapter.FixturesViewHolder>() {
    private var theTeamList = arrayList
    class FixturesViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val itemTeam1: TextView = itemView.findViewById(R.id.localTeamRun)
        val itemTeam2: TextView = itemView.findViewById(R.id.visitorTeamRun)
        val itemDate: TextView = itemView.findViewById(R.id.result)
        val itemPicture1: ImageView = itemView.findViewById(R.id.localTeamImage)
        val itemPicture2: ImageView = itemView.findViewById(R.id.VisitorTeamImage)
        val itemBellIcon:ImageFilterButton = itemView.findViewById(R.id.bellIcon)
        val cardView: CardView = itemView.findViewById(R.id.cardId)
        val itemRound: TextView = itemView.findViewById(R.id.venue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.fixtures_item_layout, parent, false)
        return FixturesAdapter.FixturesViewHolder(root)
    }


    override fun onBindViewHolder(holder: FixturesViewHolder, position: Int) {
        val currentData = theTeamList[position]
        if (!TextUtils.isEmpty(currentData.localteam.name)) {
            holder.itemTeam1.text = currentData.localteam.name
        } else {
            holder.itemTeam1.text = "Title Missing!"
        }
        if (!TextUtils.isEmpty(currentData.visitorteam.name)) {
            holder.itemTeam2.text = currentData.visitorteam.name
        } else {
            holder.itemTeam2.text = "Title Missing!"
        }

        if(currentData.note==""){

                holder.itemDate.text ="Game Will Start at  ${currentData.starting_at?.removeRange(10..26)}" ?: "No Date"

        }else{

                holder.itemDate.text =  currentData.note

        }

//        if (!TextUtils.isEmpty(currentData.starting_at)) {
//            holder.itemDate.text ="Game Will Start at  ${currentData.starting_at?.removeRange(10..26)}" ?: "No Date"
//        } else {
//            holder.itemDate.text = "Date Missing"
//        }
        if (!TextUtils.isEmpty(currentData.round)) {
            holder.itemRound.text = currentData.round
        } else {
            holder.itemRound.text = "Match Round Missing!"
        }
        if (!TextUtils.isEmpty(currentData.localteam.image_path)) {
            Picasso.get()
                .load(currentData.localteam.image_path)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                .into(holder.itemPicture1)
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
        if (!TextUtils.isEmpty(currentData.visitorteam.image_path)) {
            Picasso.get()
                .load(currentData.visitorteam.image_path)
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
        holder.itemBellIcon.setOnClickListener{
            viewModel.subscribe(context,currentData.localteam.name,currentData.visitorteam.name,currentData.id,currentData.round)
        }
        if(currentData.status=="Finished"){
            holder.cardView.setOnClickListener{
                val action =
                    com.raian.cricketeoapp.fragment.CricketeoHomeFragmentDirections.actionCricketeoHomeFragmentToScorecardFragment(currentData.id!!)
                holder.itemView.findNavController().navigate(action)
            }
        }

    }
    override fun getItemCount(): Int {
        return theTeamList.size
    }

}*/
