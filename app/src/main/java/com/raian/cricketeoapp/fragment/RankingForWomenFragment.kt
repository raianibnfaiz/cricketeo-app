package com.raian.cricketeoapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.MenRankingTabLayoutAdapter
import com.raian.cricketeoapp.adapter.WomenRankingTabLayoutAdapter

class RankingForWomenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking_for_women, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout_home)
        val viewPage = view.findViewById<ViewPager2>(R.id.view_pager2)

        val tabAdapter = WomenRankingTabLayoutAdapter(childFragmentManager, lifecycle)
        viewPage.adapter = tabAdapter
        TabLayoutMediator(tabLayout, viewPage) { tab, position ->
            tab.text = WomenRankingTabLayoutAdapter.tabList[position].title
        }.attach()
    }

}