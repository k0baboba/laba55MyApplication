package com.example.laba55myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laba55myapplication.databinding.FragmentPlanesBinding

class PlanesFragment : Fragment() {

    private var _binding: FragmentPlanesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlanesAdapter

    // activityViewModels, чтобы ViewModel был общим для всех фрагментов
    private val viewModel: PlanesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        if (viewModel.states.value == null) {
            viewModel.fetchPlanes()
        }
    }


    private fun setupRecyclerView() {
        adapter = PlanesAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }


    private fun setupObservers() {
        viewModel.states.observe(viewLifecycleOwner) { states ->
            adapter.updateData(states)
            binding.recyclerView.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false // анимацию обновления
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { // только если ошибка не null
                binding.errorLayout.visibility = View.VISIBLE
                binding.errorText.text = it
                binding.recyclerView.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
                viewModel.onErrorShown() // сообщаем ViewModel
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // ProgressBar только при первой загрузке, а не при обновлении свайпом
            if (!binding.swipeRefreshLayout.isRefreshing) {
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            // во время загрузки остального не видно
            if (isLoading) {
                binding.errorLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }


    private fun setupListeners() {
        binding.retryButton.setOnClickListener { viewModel.fetchPlanes() }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.fetchPlanes() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}