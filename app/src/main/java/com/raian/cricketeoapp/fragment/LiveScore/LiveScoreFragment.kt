package com.raian.cricketeoapp.fragment.LiveScore

import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.LiveScoreAdapter
import com.raian.cricketeoapp.database.CricketDatabase
import com.raian.cricketeoapp.models.livescores.LiveScore
import com.raian.cricketeoapp.network.NoInternetException
import com.raian.cricketeoapp.repository.CricketRepository
import com.raian.cricketeoapp.viewModel.CricketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LiveScoreFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var repository: CricketRepository
    lateinit var viewModel: CricketViewModel
    lateinit var networkImage: ImageView
    lateinit var settingBtn: Button
    private lateinit var swapRefresh: SwipeRefreshLayout
    lateinit var spinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applicationDao = CricketDatabase.getDatabase(requireContext())?.cricketDao()
        repository = applicationDao?.let { CricketRepository(it) }!!
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        networkImage = view.findViewById(R.id.noInternetPic)
        settingBtn= view.findViewById(R.id.settingBtn)
        spinner = view.findViewById(R.id.spinner)
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
    fun fetchData(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val apiResponseResult: Result<List<LiveScore>> = viewModel.getLiveScore()
            val squadList = apiResponseResult.getOrNull()
            GlobalScope.launch (Dispatchers.Main){
                spinner.visibility = View.INVISIBLE
                if(apiResponseResult.isSuccess){
                    recyclerView.adapter = LiveScoreAdapter(requireContext(), viewModel, squadList)
                    networkImage.visibility = View.INVISIBLE
                    settingBtn.visibility = View.INVISIBLE
                    recyclerView.visibility=View.VISIBLE
                    Log.d("SquadFragment", "onViewCreated: Success Adapter is set")
                }else{
                    val exception = apiResponseResult.exceptionOrNull()
                    if(exception is NoInternetException){
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