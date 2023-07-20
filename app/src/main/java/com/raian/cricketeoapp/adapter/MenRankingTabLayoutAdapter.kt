package com.raian.cricketeoapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raian.cricketeoapp.fragment.MenODIRankingFragment
import com.raian.cricketeoapp.fragment.MenT20RankingFragment
import com.raian.cricketeoapp.fragment.MenTestRankingFragment
import com.raian.cricketeoapp.models.Tab

class MenRankingTabLayoutAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle)  {
    companion object {
        val tabList = listOf(
            Tab(MenTestRankingFragment(), "Test"),
            Tab(MenODIRankingFragment(), "ODI"),
            Tab(MenT20RankingFragment(),"T20")

        )
    }
    override fun getItemCount(): Int {
        return MenRankingTabLayoutAdapter.tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        return MenRankingTabLayoutAdapter.tabList[position].fragment
    }
}