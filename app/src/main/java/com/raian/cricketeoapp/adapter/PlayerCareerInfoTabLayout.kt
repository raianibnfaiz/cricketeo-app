package com.raian.cricketeoapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raian.cricketeoapp.fragment.Career.CareerBattingFragment
import com.raian.cricketeoapp.fragment.Career.CareerBowlingFragment
import com.raian.cricketeoapp.models.Tab

class PlayerCareerInfoTabLayout(manager: FragmentManager, lifecycle: Lifecycle, val number:Int):
    FragmentStateAdapter(manager, lifecycle){
    val tabList = listOf(
        Tab(CareerBattingFragment(), "Batting"),
        Tab(CareerBowlingFragment(), "Bowling")
    )

    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        tabList[position].fragment.arguments = Bundle().apply {putInt("playerId",number)}
        return tabList[position].fragment
    }
}