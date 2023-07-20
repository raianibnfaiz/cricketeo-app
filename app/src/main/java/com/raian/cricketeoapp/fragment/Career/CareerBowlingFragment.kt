package com.raian.cricketeoapp.fragment.Career

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.raian.cricketeoapp.databinding.FragmentCareerBowlingBinding
import com.raian.cricketeoapp.viewModel.CricketViewModel


class CareerBowlingFragment : Fragment() {
    private val viewModel: CricketViewModel by viewModels()
    private lateinit var _binding: FragmentCareerBowlingBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCareerBowlingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playerId = arguments?.getInt("playerId", 0)!!
        viewModel.getPlayerCareer(playerId)
        viewModel.readPlayerCareer.observe(viewLifecycleOwner) {

            var matchesT20 = 0
            var inningsT20 = 0
            var oversBowledT20 = 0
            var maidenT20 = 0
            var wicketsT20 = 0
            var economyRateT20 = 0.0

            val bowlingT20 = it.career?.filter { it.type == "T20" }
            bowlingT20?.forEach {
                matchesT20 += it.bowling?.matches ?: 0
                inningsT20 += it.bowling?.innings ?: 0
                oversBowledT20 += (it.bowling?.overs)?.toInt() ?: 0
                maidenT20 += it.bowling?.medians?:0
                wicketsT20 += it.bowling?.wickets?:0

            }
            economyRateT20 = ((oversBowledT20*6) / wicketsT20).toDouble()
                binding.t20Matches.text = matchesT20.toString()
                binding.t20Innings.text = inningsT20.toString()
                binding.t20OversBowled.text = oversBowledT20.toString()
                binding.t20Maiden.text = maidenT20.toString()
                binding.t20Wickets.text = wicketsT20.toString()
                binding.t20Economy.text = economyRateT20.toString()

            var matchesODI = 0
            var inningsODI = 0
            var oversBowledODI = 0
            var maidenODI = 0
            var wicketsODI = 0
            var economyRateODI = 0.0
            val bowlingODI = it.career?.filter { it.type == "ODI" }
            bowlingODI?.forEach {
                matchesODI += it.bowling?.matches ?: 0
                inningsODI += it.bowling?.innings ?: 0
                oversBowledODI += (it.bowling?.overs)?.toInt() ?: 0
                maidenODI += it.bowling?.medians?:0
                wicketsODI += it.bowling?.wickets?:0
            }
            economyRateODI = ((oversBowledODI*6) / wicketsODI).toDouble()
            binding.odiMatchesPlayed.text = matchesODI.toString()
            binding.odiInningsPlayed.text = inningsODI.toString()
            binding.odiOversBowled.text = oversBowledODI.toString()
            binding.odiMaiden.text = maidenODI.toString()
            binding.odiWickets.text = wicketsODI.toString()
            binding.odiEconomy.text = economyRateODI.toString()

            var matchesTest = 0
            var inningsTest = 0
            var oversBowledTest = 0
            var maidenTest = 0
            var wicketsTest = 0
            var economyRateTest = 0.0
            val bowlingTest = it.career?.filter { it.type == "Test/5day" }
            bowlingTest?.forEach {
                matchesTest += it.bowling?.matches ?: 0
                inningsTest += it.bowling?.innings ?: 0
                oversBowledTest += (it.bowling?.overs)?.toInt() ?: 0
                maidenTest += it.bowling?.medians?:0
                wicketsTest += it.bowling?.wickets?:0
            }
            economyRateTest = ((oversBowledTest*6) / wicketsTest).toDouble()
            binding.testMatchesPlayed.text = matchesTest.toString()
            binding.testInningsPlayed.text = inningsTest.toString()
            binding.testOversBowled.text = oversBowledTest.toString()
            binding.testMaiden.text = maidenTest.toString()
            binding.testWickets.text = wicketsTest.toString()
            binding.testEconomy.text = economyRateTest.toString()
        }
        }
}