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
import com.raian.cricketeoapp.fragment.AllTeam.AllTeamFragmentDirections
import com.raian.cricketeoapp.models.Team
import com.squareup.picasso.Picasso

class AllTeamsAdapter(private val context: Context,
                      private val arrayList: List<Team>): RecyclerView.Adapter<AllTeamsAdapter.AllTeamViewHolder>() {
    private var theTeamList = arrayList

    class AllTeamViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val itemTeamType: TextView = itemView.findViewById(R.id.nameTitle)
        val itemName: TextView = itemView.findViewById(R.id.detail)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        val cardView: CardView = itemView.findViewById(R.id.cardViews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTeamViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return AllTeamsAdapter.AllTeamViewHolder(root)
    }

    override fun getItemCount(): Int {
        return theTeamList.size
    }

    override fun onBindViewHolder(holder: AllTeamViewHolder, position: Int) {
        val currentData = theTeamList[position]
        if (!TextUtils.isEmpty(currentData.name)) {
            holder.itemName.text = currentData.name
        } else {
            holder.itemName.text = "Title Missing!"
        }
        if ((currentData.national_team)) {
            holder.itemTeamType.text = "National Team"
        } else {
            holder.itemTeamType.text = "Club Team"
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
            //Toast.makeText(context, "${currentData.id}", Toast.LENGTH_SHORT).show()
            val action =
                AllTeamFragmentDirections.actionAllTeamFragmentToAllTeamMatchesFragment(currentData.id)
            holder.itemView.findNavController().navigate(action)
        }

    }
}