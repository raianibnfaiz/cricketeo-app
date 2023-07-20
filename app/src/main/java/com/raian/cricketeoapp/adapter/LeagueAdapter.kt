package com.raian.cricketeoapp.adapter

import android.content.Context
import android.text.TextUtils
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
import com.raian.cricketeoapp.fragment.League.LeagueFragmentDirections
import com.raian.cricketeoapp.models.league.League
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso

class LeagueAdapter(private val context: Context, val viewModel: CricketViewModel,
                    private val arrayList: List<League>?): RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>() {
    private var theTeamList = arrayList

    class LeagueViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val leagueName: TextView = itemView.findViewById(R.id.nameTitle)
        val leagueCode: TextView = itemView.findViewById(R.id.detail)
        val leagueLogo: ImageView = itemView.findViewById(R.id.iv_image)
        val card: CardView = itemView.findViewById(R.id.cardViews)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return LeagueAdapter.LeagueViewHolder(root)
    }

    override fun getItemCount(): Int {
        return theTeamList?.size?:0
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        val currentData = theTeamList?.get(position)

        if (currentData != null) {
            holder.leagueName.text = currentData.name
            holder.leagueCode.text = currentData.code

        }
        if (currentData != null) {
            if (!TextUtils.isEmpty(currentData.image_path)) {
                Picasso.get()
                    .load(currentData.image_path)
                    .fit()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .into(holder.leagueLogo)
            } else {
                Picasso.get()
                    .load(R.drawable.ic_connection_error)
                    .fit()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_connection_error)
                    .centerCrop(1)
                    .centerCrop()
                    .into(holder.leagueLogo)
            }
        }
        holder.card.setOnClickListener {
            Toast.makeText(context, "${currentData?.id}", Toast.LENGTH_SHORT).show()
            val action = currentData?.id?.let { it1 ->
                LeagueFragmentDirections.actionLeagueFragmentToLeagueMatchesFragment(
                    it1
                )
            }
            if (action != null) {
                holder.itemView.findNavController().navigate(action)
            }
        }
    }
}