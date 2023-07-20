package com.raian.cricketeoapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raian.cricketeoapp.fragment.*
import com.raian.cricketeoapp.models.Tab

class WomenRankingTabLayoutAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {
    companion object {
        val tabList = listOf(
            Tab(WomenODIRankingFragment(), "ODI"),
            Tab(WomenT20RankingFragment(),"T20")


        )
    }
    override fun getItemCount(): Int {
        return WomenRankingTabLayoutAdapter.tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        return WomenRankingTabLayoutAdapter.tabList[position].fragment
    }
}