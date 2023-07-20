package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.MenRankingAdapter
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.ranking.RankingTeam
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MenODIRankingFragment : Fragment() {
    lateinit var repository: CricketRepository
    lateinit var viewModel: CricketViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_men_o_d_i_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationDao = CricketDatabase.getDatabase(requireContext())?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        recyclerView = view.findViewById(R.id.LocalTeamLineuprecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        val adapterViewState = recyclerView.layoutManager?.onSaveInstanceState()
        recyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)
        recyclerView.setHasFixedSize(true)
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<List<RankingTeam>?> = viewModel.getTeamRanking("ODI","men")
            val rankingList = apiResponseResult.getOrNull()
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                if(apiResponseResult.isSuccess){
                    recyclerView.adapter = MenRankingAdapter(requireContext(), viewModel, rankingList,"men")
                    Log.d("RankingFragment", "onViewCreated: Success Adapter is set: ${rankingList}")
                }
            }
        }
    }

}