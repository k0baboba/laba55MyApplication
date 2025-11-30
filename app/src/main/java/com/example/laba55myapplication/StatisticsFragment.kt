package com.example.laba55myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.laba55myapplication.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlanesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.statistics.observe(viewLifecycleOwner) { stats ->
            binding.totalPlanes.text = "Total planes: ${stats.total}"
            binding.planesInAir.text = "Planes in air: ${stats.inAir}"
            binding.planesOnGround.text = "Planes on ground: ${stats.onGround}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}