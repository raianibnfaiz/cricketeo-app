package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.PlayersAdapter
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.SquadPlayerDetails
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel


class PlayersFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var repository: CricketRepository
    lateinit var viewModel: CricketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_players, container, false)
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
        /*lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                repository.refreshTeams()

            }
        }*/
        viewModel.readAllPlayers.observe(viewLifecycleOwner) {
            val adapterViewState = recyclerView.layoutManager?.onSaveInstanceState()
            recyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)
            recyclerView.adapter = PlayersAdapter(
                requireContext(),viewModel, it as ArrayList<SquadPlayerDetails>
            )
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        Log.d("newsTopNews", "onCreateOptionsMenu: ")
        inflater.inflate(R.menu.menu_item, menu)
        val item = menu.findItem(R.id.actionSearch)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    Log.d("newsTopNews", "onCreateOptionsMenu: $newText")
                    val adapter = recyclerView.adapter as PlayersAdapter
                    adapter.filter(newText)
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}