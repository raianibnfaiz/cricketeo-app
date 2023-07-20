package com.raian.cricketeoapp.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.UpcomingMatchFixtureAdapter

import com.raian.cricketeoapp.database.CricketDatabase

import com.raian.cricketeoapp.models.fixture.FixtureDetail

import com.raian.cricketeoapp.network.NoInternetException
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpcomingMatchesFixturesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var repository: CricketRepository
    lateinit var viewModel: CricketViewModel
    lateinit var networkImage: ImageView
    lateinit var settingBtn:Button
    lateinit var spinner: ProgressBar
    private lateinit var swapRefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_matches_fixtures, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationDao = CricketDatabase.getDatabase(requireContext())?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        networkImage = view.findViewById(R.id.noInternetPic)
        spinner = view.findViewById(R.id.spinner)
        settingBtn= view.findViewById(R.id.settingBtn)
        settingBtn.setOnClickListener{
            val intent= Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        }
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
    fun fetchData(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<List<FixtureDetail>> = viewModel.getUpcomingFixturesMatches()
            val squadList = apiResponseResult.getOrNull()
            GlobalScope.launch (Dispatchers.Main){
                spinner.visibility = View.INVISIBLE
                if(apiResponseResult.isSuccess){
                    recyclerView.adapter = UpcomingMatchFixtureAdapter(requireContext(), viewModel, squadList)
                    networkImage.visibility = View.INVISIBLE
                    settingBtn.visibility = View.INVISIBLE
                    recyclerView.visibility=View.VISIBLE
                }else{
                    val exception = apiResponseResult.exceptionOrNull()
                    if(exception is NoInternetException){
                        networkImage.visibility=View.VISIBLE
                        settingBtn.visibility=View.VISIBLE
                        recyclerView.visibility=View.INVISIBLE
                    }
                }
                swapRefresh.isRefreshing = false
            }


        }
    }
}