package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.LineupAdapter

import com.raian.cricketeoapp.adapter.ScorecardTabLayoutAdapter
import com.raian.cricketeoapp.models.Lineup
import com.raian.cricketeoapp.models.MatchResult
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScorecardHostFragment : Fragment() {
val args: ScorecardHostFragmentArgs by navArgs()
    private val viewModel: CricketViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scorecard_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val  localTeamScores = view.findViewById<TextView>(R.id.localTeamRun)
        val localTeamName = view.findViewById<TextView>(R.id.nameLocalTeam)
        val  visitorTeamScores= view.findViewById<TextView>(R.id.visitorTeamRun)
        val visitorTeamName = view.findViewById<TextView>(R.id.nameVisitorTeam)
        val localTeamImageView = view.findViewById<ImageView>(R.id.localTeamImage)
        val visitorTeamImageView = view.findViewById<ImageView>(R.id.VisitorTeamImage)
        val matchResult = view.findViewById<TextView>(R.id.result)
        val venueName = view.findViewById<TextView>(R.id.venue)
        val league = view.findViewById<TextView>(R.id.league)
        val localTeamTitle= view.findViewById<TextView>(R.id.localTeamName)
        val visitorTeamTitle= view.findViewById<TextView>(R.id.visitorTeamName)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout_home)
        val viewPage = view.findViewById<ViewPager2>(R.id.view_pager2)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<MatchResult> = viewModel.getScoreboard(args.matchId)
            val scoreCard = apiResponseResult.getOrNull()?.note
            val localTeam = apiResponseResult.getOrNull()?.localteam?.name
            val visitorTeam = apiResponseResult.getOrNull()?.visitorteam?.name
            val visitorTeamImage = apiResponseResult.getOrNull()?.visitorteam?.image_path
            val localTeamimage = apiResponseResult.getOrNull()?.localteam?.image_path
            val localTeamId = apiResponseResult.getOrNull()?.localteam_id
            val visitorTeamId = apiResponseResult.getOrNull()?.visitorteam_id
            val scores = apiResponseResult.getOrNull()?.scoreboards
            val fixturesId = apiResponseResult.getOrNull()?.id
            val venue = apiResponseResult.getOrNull()?.venue?.name
            val leagueName = apiResponseResult.getOrNull()?.league?.name
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
                if (!TextUtils.isEmpty(localTeamimage)) {
                    Picasso.get()
                        .load(localTeamimage)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .into(localTeamImageView)
                } else {
                    Picasso.get()
                        .load(R.drawable.ic_connection_error)
                        .fit()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .centerCrop(1)
                        .centerCrop()
                        .into(localTeamImageView)
                }
                if (!TextUtils.isEmpty(visitorTeamImage)) {
                    Picasso.get()
                        .load(visitorTeamImage)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .into(visitorTeamImageView)
                } else {
                    Picasso.get()
                        .load(R.drawable.ic_connection_error)
                        .fit()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error)
                        .centerCrop(1)
                        .centerCrop()
                        .into(visitorTeamImageView)
                }
                if (!TextUtils.isEmpty(localTeam)) {
                    localTeamName.text = localTeam
                } else {
                    localTeamName.text = "Name Missing!"
                }
                if (!TextUtils.isEmpty(visitorTeam)) {
                    visitorTeamName.text = visitorTeam
                } else {
                    visitorTeamName.text = "Name Missing!"
                }
                if (!TextUtils.isEmpty(scoreCard)) {
                    matchResult.text = scoreCard
                } else {
                    matchResult.text = "Scorecard Missing!"
                }
                if (!TextUtils.isEmpty(localTeamFinalScore)) {
                    localTeamScores.text = localTeamFinalScore
                } else {
                    localTeamScores.text = "Score Missing!"
                }
                if (!TextUtils.isEmpty(visitorTeamFinalScore)) {
                    visitorTeamScores.text = visitorTeamFinalScore
                } else {
                    visitorTeamScores.text = "Score Missing!"
                }
                if (!TextUtils.isEmpty(venue)) {
                    venueName.text = "Venue: $venue"
                } else {
                    venueName.text = "Venue Missing!"
                }
                if (!TextUtils.isEmpty(leagueName)) {
                    league.text = "$leagueName"
                } else {
                    league.text = "League Missing!"
                }

            }
        }
        val tabAdapter = ScorecardTabLayoutAdapter(childFragmentManager, lifecycle,args.matchId)
        viewPage.adapter = tabAdapter
        TabLayoutMediator(tabLayout, viewPage) { tab, position ->
            tab.text = tabAdapter.tabList[position].title
        }.attach()
    }
}