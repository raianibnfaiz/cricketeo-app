package com.raian.cricketeoapp.adapter

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
import com.raian.cricketeoapp.fragment.SquadFragmentDirections
import com.raian.cricketeoapp.models.SquadPlayerDetails
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso

class SquadAdapter(private val context: Context,
                   private val viewModel: CricketViewModel,
                   private val arrayList: List<SquadPlayerDetails>?
): RecyclerView.Adapter<SquadAdapter.SquadViewHolder>() {
    private var theTeamList = arrayList

    class SquadViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        val itemName: TextView = itemView.findViewById(R.id.detail)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        val cardView: CardView = itemView.findViewById(R.id.cardViews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return SquadAdapter.SquadViewHolder(root)
    }

    override fun getItemCount(): Int {
       return theTeamList?.size ?: 0
    }

    override fun onBindViewHolder(holder: SquadViewHolder, position: Int) {
        val currentData = theTeamList?.get(position)
        if (currentData != null) {
            holder.itemName.text = currentData.fullname
        }
        if (!TextUtils.isEmpty(currentData?.image_path)) {
            Picasso.get()
                .load(currentData?.image_path)
                .fit()
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
            val action = currentData?.id?.let { it1 ->
                SquadFragmentDirections.actionSquadFragmentToPlayerCareerFragment(
                    it1
                )
            }
            if (action != null) {
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

}