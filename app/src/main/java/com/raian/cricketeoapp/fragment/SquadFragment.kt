package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.SquadAdapter
import com.raian.cricketeoapp.models.Squad
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SquadFragment : Fragment() {
    val args : SquadFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private val viewModel: CricketViewModel by viewModels()
    lateinit var spinner: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_squad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.spinner)
        recyclerView = view.findViewById(R.id.LocalTeamLineuprecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapterViewState = recyclerView.layoutManager?.onSaveInstanceState()
        recyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<Squad> = viewModel.getSquad(args.squadID)
            val squadList = apiResponseResult.getOrNull()?.squad
            GlobalScope.launch (Dispatchers.Main){
                spinner.visibility = View.INVISIBLE
                if(apiResponseResult.isSuccess){
                    recyclerView.adapter = SquadAdapter(requireContext(), viewModel, squadList)
                    Log.d("SquadFragment", "onViewCreated: Success Adapter is set")
                }
            }


        }
    }

}