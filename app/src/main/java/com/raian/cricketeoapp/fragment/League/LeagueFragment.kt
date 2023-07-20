package com.raian.cricketeoapp.fragment.League

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
import com.raian.cricketeoapp.adapter.LeagueAdapter
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.league.League
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "LeagueFragment"
class LeagueFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var repository: CricketRepository
    lateinit var viewModel: CricketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_league, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationDao = CricketDatabase.getDatabase(requireContext())?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        recyclerView = view.findViewById(R.id.rv_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<List<League>> = viewModel.getLeagues()
            val squadList = apiResponseResult.getOrNull()
            GlobalScope.launch (Dispatchers.Main){
                if(apiResponseResult.isSuccess){
                    recyclerView.adapter = LeagueAdapter(requireContext(), viewModel, squadList)
                    Log.d("SquadFragment", "onViewCreated: Success Adapter is set")
                }else{
                    val exception = apiResponseResult.exceptionOrNull()
                    Log.d(TAG, "onViewCreated: $exception")
                }
            }


        }
    }
}