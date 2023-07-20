package com.raian.cricketeoapp.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.raian.cricketeoapp.R
import com.raian.cricketeoapp.adapter.PlayerCareerInfoTabLayout
import com.raian.cricketeoapp.adapter.ScorecardTabLayoutAdapter
import com.raian.cricketeoapp.databinding.FragmentPlayerCareerBinding
import com.raian.cricketeoapp.viewModel.CricketViewModel
import com.squareup.picasso.Picasso

private const val TAG = "PlayerCareerFragment"

class PlayerCareerFragment : Fragment() {

    val args: PlayerCareerFragmentArgs by navArgs()
    private val viewModel: CricketViewModel by viewModels()
    private lateinit var _binding: FragmentPlayerCareerBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerCareerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.getPlayerCareer(args.playerId)

            viewModel.readPlayerCareer.observe(viewLifecycleOwner) {
                nameTitle.text = it.fullname
                it.country_id?.let { it1 ->
                    viewModel.getCountryName(it1).observe(viewLifecycleOwner) {

                        if (it != null) {
                            detail.text = it
                        }else{
                            detail.text="Unknown"
                        }
                    }
                }

                if (!TextUtils.isEmpty(it.image_path)) {
                    Picasso.get().load(it.image_path).placeholder(R.drawable.loading_animation)
                        .fit()
                        .error(R.drawable.ic_connection_error).into(ivImage)
                } else {
                    Picasso.get().load(R.drawable.ic_connection_error).fit()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_connection_error).centerCrop(1).centerCrop()
                        .into(ivImage)
                }
            }
        }
        val tabAdapter = PlayerCareerInfoTabLayout(childFragmentManager, lifecycle, args.playerId)
        binding.viewPager2.adapter = tabAdapter
        TabLayoutMediator(binding.tabLayoutHome, binding.viewPager2) { tab, position ->
            tab.text = tabAdapter.tabList[position].title
        }.attach()

    }

}