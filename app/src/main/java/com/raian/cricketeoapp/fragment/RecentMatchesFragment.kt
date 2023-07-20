package com.raian.cricketeoapp.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.RecentMatchAdapter
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.fixture.FixtureDetail
import com.raian.cricketeoapp.network.NoInternetException
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RecentMatchesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var repository: CricketRepository
    lateinit var viewModel: CricketViewModel
    lateinit var networkImage: ImageView
    lateinit var settingBtn: Button
    private lateinit var swapRefresh: SwipeRefreshLayout
    lateinit var spinner: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_matches, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkImage = view.findViewById(R.id.noInternetPic)
        settingBtn = view.findViewById(R.id.settingBtn)
        spinner = view.findViewById(R.id.spinner)
        settingBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        }
        val applicationDao = CricketDatabase.getDatabase(requireContext())?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        //viewModel.getRecentFixturesMatches()
        recyclerView = view.findViewById(R.id.rv_recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        val adapterViewState = recyclerView.layoutManager?.onSaveInstanceState()
        recyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)
        recyclerView.setHasFixedSize(true)

        fetchData()
        swapRefresh = view.findViewById(R.id.swipeRefreshLayout)
        swapRefresh.setOnRefreshListener {
            fetchData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<List<FixtureDetail>> =
                viewModel.getRecentFixturesMatches()
            val squadList = apiResponseResult.getOrNull()
            GlobalScope.launch(Dispatchers.Main) {
                spinner.visibility = View.INVISIBLE
                if (apiResponseResult.isSuccess) {
                    recyclerView.adapter =
                        RecentMatchAdapter(requireContext(), viewModel, squadList)
                    Log.d("SquadFragment", "onViewCreated: Success Adapter is set")
                    networkImage.visibility = View.INVISIBLE
                    settingBtn.visibility = View.INVISIBLE
                    recyclerView.visibility=View.VISIBLE
                } else {
                    val exception = apiResponseResult.exceptionOrNull()
                    if (exception is NoInternetException) {
                        networkImage.visibility = View.VISIBLE
                        settingBtn.visibility = View.VISIBLE
                        recyclerView.visibility=View.INVISIBLE
                    }
                }
                swapRefresh.isRefreshing = false
            }


        }
    }

}