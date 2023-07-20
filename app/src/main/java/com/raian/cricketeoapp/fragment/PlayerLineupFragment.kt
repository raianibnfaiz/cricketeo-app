package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.LineupAdapter
import com.raian.cricketeoapp.models.Lineup
import com.raian.cricketeoapp.models.MatchResult
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerLineupFragment : Fragment() {
    //val args: com.raian.cricketeoapp.fragment.ScorecardFragmentArgs by navArgs()
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private val viewModel: CricketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_lineup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val  localTeamScores = view.findViewById<TextView>(R.id.localTeamRun)
//        val localTeamName = view.findViewById<TextView>(R.id.nameLocalTeam)
        //val  visitorTeamScores= view.findViewById<TextView>(R.id.visitorTeamRun)
        //val visitorTeamName = view.findViewById<TextView>(R.id.nameVisitorTeam)
        //val localTeamImageView = view.findViewById<ImageView>(R.id.localTeamImage)
        //val visitorTeamImageView = view.findViewById<ImageView>(R.id.VisitorTeamImage)
        //val matchResult = view.findViewById<TextView>(R.id.result)
        //val venueName = view.findViewById<TextView>(R.id.venue)
        //val league = view.findViewById<TextView>(R.id.league)
        val localTeamTitle= view.findViewById<TextView>(R.id.localTeamName)
        val visitorTeamTitle= view.findViewById<TextView>(R.id.visitorTeamName)

        recyclerView1 = view.findViewById(R.id.localTeamPlayer)
        recyclerView1.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView1.setHasFixedSize(true)

        recyclerView2 = view.findViewById(R.id.visitorTeamPlayer)
        recyclerView2.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView2.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val matchId=arguments?.getInt("matchId",0)!!
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
            val batting= apiResponseResult.getOrNull()?.batting
            var localTeamFinalScore = ""
            var locaTeamPlayedOver = ""
            var visitorTeamFinalScore = ""
            var visitorTeamPlayedOver = ""
            Log.d("Local Team id", "onViewCreated: ${localTeamId}, ${fixturesId}")
            val local = scores?.filter { it.team_id == localTeamId }
            val visitor = scores?.filter { it.team_id == visitorTeamId }
            val localTeamLineup = lineup?.filter { it.lineup?.team_id == localTeamId }
            val localTeamLineMap = HashMap<Int, Lineup>()
            localTeamLineup?.forEach { lineup -> lineup.id?.let { localTeamLineMap[it] = lineup } }
            val id = 123
            localTeamLineMap[123]
            val visitorTeamLineup = lineup?.filter { it.lineup?.team_id == visitorTeamId }
            val localTeamBatting=batting?.filter { it.team_id==localTeamId }
            if (localTeamBatting != null) {
                for (i in localTeamBatting){
                    val playerRun=localTeamLineMap[i.player_id]
                    Log.d("Batting", "Run: ${playerRun?.fullname} : ${i.score}")
                }
            }
            if (localTeamLineup != null) {
                for (i in localTeamLineup) {
                    Log.d("Local-Team-lineup", "onViewCreated: ${i.fullname}")
                }
            }
            if (local != null) {
                for (i in local) {
                    if (i.type == "total") {
                        Log.d("Score", "local Team total score: ${i.total}/${i.wickets}")
                    }
                    localTeamFinalScore = "${i.total}/${i.wickets}"
                }
            }
            if (visitor != null) {
                for (i in visitor) {
                    if (i.type == "total") {
                        Log.d("Score", "Visitor Team total score: ${i.total}/${i.wickets}")
                        visitorTeamFinalScore = "${i.total}/${i.wickets}"
                    }
                }
            }
            if (local != null) {
                for (i in local) {
                    if (i.type == "total") {
                        Log.d("Score", "local Team total score:${i.team_id}: ${i.overs}")
                    }
                    locaTeamPlayedOver = "${i.overs}"
                    localTeamFinalScore = "${i.total}/${i.wickets}(${i.overs})"
                    Log.d("Localover", "onViewCreated: $locaTeamPlayedOver")
                }
            }
            if (visitor != null) {
                for (i in visitor) {
                    if (i.type == "total") {
                        Log.d("Score", "local Team total score:${i.team_id}: ${i.overs}")
                    }
                    visitorTeamPlayedOver = "${i.overs}"
                    visitorTeamFinalScore = "${i.total}/${i.wickets}(${i.overs})"
                    Log.d("visitorover", "onViewCreated: $visitorTeamPlayedOver")
                }
            }

            withContext(Dispatchers.Main) {
                recyclerView1.adapter = LineupAdapter(localTeamLineup)
                recyclerView2.adapter = LineupAdapter(visitorTeamLineup)
                if (!TextUtils.isEmpty(localTeam)) {
                    localTeamTitle.text = "$localTeam"
                } else {
                    localTeamTitle.text = "Name Missing!"
                }
                if (!TextUtils.isEmpty(visitorTeam)) {
                    visitorTeamTitle.text = "$visitorTeam"
                } else {
                    visitorTeamTitle.text = "Name Missing!"
                }
            }
        }
        }
    }