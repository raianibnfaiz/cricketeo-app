package com.raian.cricketeoapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.fragment.PlayersFragmentDirections
import com.raian.cricketeoapp.models.SquadPlayerDetails
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso
import java.util.*

class PlayersAdapter(
    private val context: Context,
    private val viewModel: CricketViewModel,
    private val arrayList: List<SquadPlayerDetails>
) : RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {
    private var theTeamList = arrayList
    class PlayerViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        val itemTeamType: TextView = itemView.findViewById(R.id.nameTitle)
        val itemName: TextView = itemView.findViewById(R.id.detail)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        val cardView: CardView = itemView.findViewById(R.id.cardViews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return PlayersAdapter.PlayerViewHolder(root)
    }

    override fun getItemCount(): Int {
        return theTeamList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {

        val currentData = theTeamList[position]
        if (!TextUtils.isEmpty(currentData.fullname)) {
            holder.itemName.text = currentData.fullname
        } else {
            holder.itemName.text = "Title Missing!"
        }


        if (!TextUtils.isEmpty(currentData.image_path)) {
            Picasso.get()
                .load(currentData.image_path)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                .into(holder.itemPicture)
        } else {
            Picasso.get()
                .load(R.drawable.ic_connection_error)
                .fit()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                .centerCrop(1)
                .centerCrop()
                .into(holder.itemPicture)
        }
        holder.cardView.setOnClickListener {

            viewModel.getPlayerCareer(currentData.id!!)
            val action =
                PlayersFragmentDirections.actionPlayersFragmentToPlayerCareerFragment(currentData.id)
            holder.itemView.findNavController().navigate(action)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun filterList(filterlist: List<SquadPlayerDetails>) {
        theTeamList = filterlist
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filter(text: String) {
        val filteredList = ArrayList<SquadPlayerDetails>()
        for (article in arrayList) {
            if (article.fullname?.lowercase(Locale.ROOT)
                    ?.contains(text.lowercase(Locale.ROOT)) == true
            ) {
                filteredList.add(article)
                notifyDataSetChanged()
            }
        }
        filterList(filteredList)
    }

}