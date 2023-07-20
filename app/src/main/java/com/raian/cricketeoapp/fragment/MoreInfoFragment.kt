package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raian.cricketeoapp.R

class MoreInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menRanking: TextView = view.findViewById(R.id.menRanking)
        val womenRanking: TextView = view.findViewById(R.id.womenRanking)
        val players: TextView = view.findViewById(R.id.players)
        val leagues: TextView = view.findViewById(R.id.league)
        val teams: TextView = view.findViewById(R.id.teams)
        menRanking.setOnClickListener {
            val action =
                MoreInfoFragmentDirections.actionMoreInfoFragmentToRankingForMenFragment()
            findNavController().navigate(action)
        }
        womenRanking.setOnClickListener {
            val action =
                MoreInfoFragmentDirections.actionMoreInfoFragmentToRankingForWomenFragment()
            findNavController().navigate(action)
        }
        players.setOnClickListener {
            val action = MoreInfoFragmentDirections.actionMoreInfoFragmentToPlayersFragment()
            findNavController().navigate(action)
        }
        leagues.setOnClickListener{
            val action = MoreInfoFragmentDirections.actionMoreInfoFragmentToLeagueFragment()
            findNavController().navigate(action)
        }
        teams.setOnClickListener{
            val action = MoreInfoFragmentDirections.actionMoreInfoFragmentToAllTeamFragment()
            findNavController().navigate(action)
        }
    }
}