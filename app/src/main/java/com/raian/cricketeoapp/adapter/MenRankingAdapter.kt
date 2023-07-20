package com.raian.cricketeoapp.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.models.ranking.RankingTeam
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso

private const val TAG = "MenRankingAdapter"
class MenRankingAdapter(private val context: Context,
                        private val viewModel: CricketViewModel,
                        private val arrayList: List<RankingTeam>?,
                        private val gender:String
): RecyclerView.Adapter<MenRankingAdapter.MenRankingViewHolder>() {
    private var theTeamList = arrayList
//    var team = mutableListOf<RankingTeam>()
//    var teamName = ""
//    var teamPosition = ""
    class MenRankingViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        val itemTeamName: TextView = itemView.findViewById(R.id.detail)
        val itemPosition: TextView = itemView.findViewById(R.id.nameTitle)
        val imageFlag: ImageView = itemView.findViewById(R.id.iv_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenRankingViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MenRankingViewHolder(root)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${theTeamList?.size}")
        return theTeamList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MenRankingViewHolder, position: Int) {
        val currentData = theTeamList?.get(position)
        Log.d("Rankinngg", "onBindViewHolder: $arrayList")
        if (currentData != null) {
            holder.itemTeamName.text=currentData.name
        }
        if (currentData != null) {
            holder.itemPosition.text=currentData.position.toString()
        }
        if (!TextUtils.isEmpty(currentData?.image_path)) {
            Picasso.get()
                .load(currentData?.image_path)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                .into(holder.imageFlag)
        } else {
            Picasso.get()
                .load(R.drawable.ic_connection_error)
                .fit()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                .centerCrop(1)
                .centerCrop()
                .into(holder.imageFlag)
        }

       /* for (i in currentData.toString()) {
            Log.d("Rankinngg", "onBindViewHolder: ${i}")
            //Log.d("Rankinngg", "onBindViewHolder: ${i.position}")


        }*/
        if (currentData != null) {
            Log.d("this is Team", "onBindViewHolder: ${currentData.name}")
        }
       /* GlobalScope.launch(Dispatchers.Default) {
                for (i in currentData.team!!) {
                    Log.d(TAG, "onBindViewHolder: ${i.name}")
                    Log.d(TAG, "onBindViewHolder: ${i.position}")
                    teamName= i.name.toString()
                    teamPosition = i.position.toString()
                }
        }

        holder.itemTeamName.text = teamName
        holder.itemPosition.text = teamPosition*/

        /*if (currentData?.gender == "men") {
            currentData?.team?.map {
                holder.itemTeamName.text = it.name
                holder.itemPosition.text = it.position.toString()

                Log.d("Rank Team", "onBindViewHolder: ${it.name}")
            }
        }*/
    }
}