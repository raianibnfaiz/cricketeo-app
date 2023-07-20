package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.BattingAdapter
import com.raian.cricketeoapp.adapter.BowlingAdapter
import com.raian.cricketeoapp.models.Lineup
import com.raian.cricketeoapp.models.MatchResult
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MatchScorecardFragment : Fragment() {

    private lateinit var battingRecyclerview: RecyclerView
    private lateinit var bowlingRecyclerView: RecyclerView
    lateinit var localTeamInnings:TextView
    lateinit var visitorTeamInnings:TextView
    private var currentInningsTab = 1
    lateinit var manofthematchPlayerImage:ImageView
    lateinit var manofthematchPlayerName:TextView
    lateinit var manofthematchPlayerPosition:TextView
    lateinit var spinner:ProgressBar
    private val viewModel: CricketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("argument", "onCreate: ${arguments?.getInt("matchId",)}")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_scorecard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        battingRecyclerview = view.findViewById(R.id.battingRecyclerview)
        bowlingRecyclerView = view.findViewById(R.id.bowlingRecyclerview)
        localTeamInnings=view.findViewById(R.id.localTeamInnings)
        visitorTeamInnings=view.findViewById(R.id.visitorTeamInnings)
        manofthematchPlayerName=view.findViewById(R.id.manofthematchPlayerName)
        manofthematchPlayerImage=view.findViewById(R.id.manofthematchImage)
        spinner = view.findViewById(R.id.spinner)
        battingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        bowlingRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        battingRecyclerview.setHasFixedSize(true)
        bowlingRecyclerView.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val matchId = arguments?.getInt("matchId", 0)!!
            val apiResponseResult: Result<MatchResult> = viewModel.getScoreboard(matchId)
            val scoreCard = apiResponseResult.getOrNull()?.note
            val localTeam = apiResponseResult.getOrNull()?.localteam?.name
            val visitorTeam = apiResponseResult.getOrNull()?.visitorteam?.name
            val visitorTeamImage = apiResponseResult.getOrNull()?.visitorteam?.image_path
            val localTeamimage = apiResponseResult.getOrNull()?.localteam?.image_path
            val localTeamId = apiResponseResult.getOrNull()?.localteam_id
            val visitorTeamId = apiResponseResult.getOrNull()?.visitorteam_id
            val scores = apiResponseResult.getOrNull()?.scoreboards
            val fixturesId = apiResponseResult.getOrNull()?.id
            val lineup = apiResponseResult.getOrNull()?.lineup
            val batting = apiResponseResult.getOrNull()?.batting
            val bowling = apiResponseResult.getOrNull()?.bowling

            val localTeamLineup = lineup?.filter { it.lineup?.team_id == localTeamId }
            val localTeamLineMap = HashMap<Int, Lineup>()
            localTeamLineup?.forEach { lineup -> lineup.id?.let { localTeamLineMap[it] = lineup } }
            val visitorTeamLineup = lineup?.filter { it.lineup?.team_id == visitorTeamId }
            val localTeamBatting = batting?.filter { it.team_id == localTeamId }
            val localTeamBowling = bowling?.filter { it.team_id == localTeamId }
            val visitorTeamBatting= batting?.filter{ it.team_id == visitorTeamId}
            val visitorTeamBowling = bowling?.filter{ it.team_id == visitorTeamId}
            if (localTeamBatting != null) {
                for (i in localTeamBatting) {
                    val playerRun = localTeamLineMap[i.player_id]
                    Log.d("Batting", "Run: ${playerRun?.fullname} : ${i.score}")
                }
            }
            fun selectInnings(){
                when(currentInningsTab){
                    1 -> {
                        localTeamInnings.isSelected = true
                        localTeamInnings.setBackgroundResource(R.color.tabcolorbox)
                        localTeamInnings.setTextColor(ContextCompat.getColor(requireContext(), R.color.tabtextcolor))

                        visitorTeamInnings.isSelected = false
                        visitorTeamInnings.setBackgroundResource(R.drawable.baseline_border_style_24)
                        visitorTeamInnings.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))
                    }
                    2 -> {
                        visitorTeamInnings.isSelected = true
                        visitorTeamInnings.setBackgroundResource(R.color.tabcolorbox)
                        visitorTeamInnings.setTextColor(ContextCompat.getColor(requireContext(), R.color.tabtextcolor))

                        localTeamInnings.isSelected = false
                        localTeamInnings.setBackgroundResource(R.drawable.baseline_border_style_24)
                        localTeamInnings.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))
                    }
                }
            }

            withContext(Dispatchers.Main) {
                spinner.visibility = View.INVISIBLE;
                localTeamInnings.text=localTeam
                visitorTeamInnings.text=visitorTeam
                manofthematchPlayerName.text=apiResponseResult.getOrNull()?.manofmatch?.fullname
                if (!TextUtils.isEmpty(localTeamimage)) {
                    Picasso.get()
                        .load(apiResponseResult.getOrNull()?.manofmatch?.image_path)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .into(manofthematchPlayerImage)
                } else {
                    Picasso.get()
                        .load(R.drawable.ic_connection_error)
                        .fit()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .centerCrop(1)
                        .centerCrop()
                        .into(manofthematchPlayerImage)
                }
                currentInningsTab=1
                selectInnings()
                battingRecyclerview.adapter = BattingAdapter(localTeamLineup,localTeamBatting)
                bowlingRecyclerView.adapter = BowlingAdapter(localTeamLineup,localTeamBowling)
                localTeamInnings.setOnClickListener{
                    currentInningsTab=1
                    selectInnings()
                    battingRecyclerview.adapter = BattingAdapter(localTeamLineup,localTeamBatting)
                    bowlingRecyclerView.adapter = BowlingAdapter(localTeamLineup,localTeamBowling)

                }
                visitorTeamInnings.setOnClickListener{
                    currentInningsTab=2
                    selectInnings()
                    battingRecyclerview.adapter = BattingAdapter(visitorTeamLineup,visitorTeamBatting)
                    bowlingRecyclerView.adapter = BowlingAdapter(visitorTeamLineup,visitorTeamBowling)
                }

            }
        }
    }
}