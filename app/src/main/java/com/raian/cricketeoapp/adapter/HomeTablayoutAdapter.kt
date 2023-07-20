package com.raian.cricketeoapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raian.cricketeoapp.fragment.LiveScore.LiveScoreFragment
import com.raian.cricketeoapp.fragment.UpcomingMatchesFixturesFragment
import com.raian.cricketeoapp.fragment.RecentMatchesFragment
import com.raian.cricketeoapp.models.Tab

class HomeTablayoutAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {
    companion object {
        val tabList = listOf(
            Tab(UpcomingMatchesFixturesFragment(), "Upcoming Matches"),
            Tab(RecentMatchesFragment(), "Recent Matches"),
            Tab(LiveScoreFragment(), "Live Matches")

        )
    }
    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabList[position].fragment
    }
}