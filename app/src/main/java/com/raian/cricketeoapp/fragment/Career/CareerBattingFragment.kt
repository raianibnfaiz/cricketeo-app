package com.raian.cricketeoapp.fragment.Career

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.raian.cricketeoapp.databinding.FragmentCareerBattingBinding
import com.raian.cricketeoapp.viewModel.CricketViewModel


class CareerBattingFragment : Fragment() {
    private val viewModel: CricketViewModel by viewModels()
    private lateinit var _binding: FragmentCareerBattingBinding
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCareerBattingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playerId = arguments?.getInt("playerId", 0)!!
        viewModel.getPlayerCareer(playerId)
        viewModel.readPlayerCareer.observe(viewLifecycleOwner) {

            var matchesT20 = 0
            var inningsT20 = 0
            var ballsT20 = 0
            var runsT20 = 0
            var averageT20 = 0.0

            val batting = it.career?.filter { it.type == "T20" }
            batting?.forEach {
                matchesT20 += it.batting?.matches ?: 0
                inningsT20 += it.batting?.innings ?: 0
                ballsT20 += it.batting?.balls_faced ?: 0
                runsT20 += it.batting?.runs_scored ?: 0

            }
                averageT20 = (runsT20/matchesT20).toDouble()

            binding.t20MatchesPlayed.text = matchesT20.toString()
            binding.t20InningsPlayed.text = inningsT20.toString()
            binding.t20BallsPlayed.text = ballsT20.toString()
            binding.t20RunsScored.text = runsT20.toString()
            binding.t20Average.text = averageT20.toString()

            var matchesODI = 0
            var inningsODI = 0
            var ballsODI = 0
            var runsODI = 0
            var averageODI = 0.0

            val battingODI = it.career?.filter { it.type == "ODI" }
            battingODI?.forEach {
                matchesODI += it.batting?.matches ?: 0
                inningsODI += it.batting?.innings ?: 0
                ballsODI += it.batting?.balls_faced ?: 0
                runsODI += it.batting?.runs_scored ?: 0
                if (it.batting?.average != null) {

                    averageODI =
                        (runsODI/matchesODI).toDouble()

                }
            }
            binding.odiMatchesPlayed.text = matchesODI.toString()
            binding.odiInningsPlayed.text = inningsODI.toString()
            binding.odiBallsPlayed.text = ballsODI.toString()
            binding.odiRunsScored.text = runsODI.toString()
            binding.odiAverage.text = averageODI.toString()

            var matchesTEST = 0
            var inningsTEST = 0
            var ballsTEST = 0
            var runsTEST = 0
            var averageTEST = 0.0

            val battingTEST = it.career?.filter { it.type == "Test/5day" }
            battingTEST?.forEach {
                matchesTEST += it.batting?.matches ?: 0
                inningsTEST += it.batting?.innings ?: 0
                ballsTEST += it.batting?.balls_faced ?: 0
                runsTEST += it.batting?.runs_scored ?: 0
                if (it.batting?.average != null) {

                    averageTEST =
                        (runsTEST/matchesTEST).toDouble()

                }
            }
            binding.testMatchesPlayed.text = matchesTEST.toString()
            binding.testInningsPlayed.text = inningsTEST.toString()
            binding.testBallsPlayed.text = ballsTEST.toString()
            binding.testRunsScored.text = runsTEST.toString()
            binding.testAverage.text = averageTEST.toString()
        }
    }

}
