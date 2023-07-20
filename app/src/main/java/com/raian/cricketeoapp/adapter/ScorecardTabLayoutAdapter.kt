package com.raian.cricketeoapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raian.cricketeoapp.fragment.PlayerLineupFragment
import com.raian.cricketeoapp.fragment.MatchScorecardFragment
import com.raian.cricketeoapp.models.Tab

class ScorecardTabLayoutAdapter(manager: FragmentManager, lifecycle: Lifecycle,val number:Int):FragmentStateAdapter(manager, lifecycle){
    val tabList = listOf(
        Tab(MatchScorecardFragment(), "Scorecard"),
        Tab(PlayerLineupFragment(), "Lineup"))


    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        tabList[position].fragment.arguments = Bundle().apply {putInt("matchId",number)}
        return tabList[position].fragment
   }
}