package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.TeamsAdapter
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.Team
import com.raian.cricketeoapp.network.NoInternetException
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var repository: CricketRepository
    var listNews = ArrayList<Team>()
    lateinit var viewModel: CricketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationDao = CricketDatabase.getDatabase(requireContext())?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        recyclerView = view.findViewById(R.id.rv_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
//        val adapter = CricketAdapter(
//            requireContext(), viewModel, listNews
//        )
        val adapterViewState = recyclerView.layoutManager?.onSaveInstanceState()
        recyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)
        //recyclerView.adapter = adapter
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.refreshTeams()
                }catch (e: NoInternetException){
                    Log.d("error checking", "no internet: $e")
                } catch (e:java.lang.Exception){
                    Log.d("error checking", "other unexpected error: $e")
                }


            }
        }
        viewModel.readAllTeamContainsSquad.observe(viewLifecycleOwner) {
            val adapterViewState = recyclerView.layoutManager?.onSaveInstanceState()
            recyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)
            recyclerView.adapter = TeamsAdapter(
                requireContext(), it as ArrayList<Team>
            )
        }
    }

}