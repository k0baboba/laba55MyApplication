package com.example.laba55myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import com.example.laba55myapplication.databinding.FragmentDatabaseBinding

class DatabaseFragment : Fragment() {

    private var _binding: FragmentDatabaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DatabaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        val planeDao = db.planeDao()

        adapter = DatabaseAdapter(
            onItemClick = { plane -> showDetails(plane) },

            onItemLongClick = { plane -> showActionDialog(plane) }
        )

        binding.recyclerViewDb.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDb.adapter = adapter

        planeDao.getAllPlanes().observe(viewLifecycleOwner) { planes ->
            adapter.submitList(planes)
        }

        binding.fabAddPlane.setOnClickListener {
            // Activity для добавления
            val intent = Intent(requireContext(), AddPlaneActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showActionDialog(plane: PlaneEntity) {
        val options = arrayOf("View Details", "Update", "Delete")

        AlertDialog.Builder(requireContext())
            .setTitle("Actions for ${plane.icao24}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showDetails(plane)
                    1 -> updatePlane(plane)
                    2 -> deletePlane(plane)
                }
            }
            .show()
    }

    private fun showDetails(plane: PlaneEntity) {
        val stateVector = plane.toStateVector()
        val bundle = Bundle().apply {
            putParcelable("plane", stateVector)
        }
        findNavController().navigate(R.id.planeDetailFragment, bundle)
    }

    private fun updatePlane(plane: PlaneEntity) {
        val intent = Intent(requireContext(), UpdatePlaneActivity::class.java).apply {
            putExtra(UpdatePlaneActivity.EXTRA_ICAO, plane.icao24)
            putExtra(UpdatePlaneActivity.EXTRA_CALLSIGN, plane.callsign)
            putExtra(UpdatePlaneActivity.EXTRA_COUNTRY, plane.originCountry)
            putExtra(UpdatePlaneActivity.EXTRA_VELOCITY, plane.velocity)
            putExtra(UpdatePlaneActivity.EXTRA_ALTITUDE, plane.baroAltitude)
            
            putExtra(UpdatePlaneActivity.EXTRA_LONGITUDE, plane.longitude)
            putExtra(UpdatePlaneActivity.EXTRA_LATITUDE, plane.latitude)
            putExtra(UpdatePlaneActivity.EXTRA_TRUE_TRACK, plane.trueTrack)
            putExtra(UpdatePlaneActivity.EXTRA_ON_GROUND, plane.onGround)
        }
        startActivity(intent)
    }

    private fun deletePlane(plane: PlaneEntity) {
        val intent = Intent(requireContext(), DeletePlaneActivity::class.java).apply {
            putExtra(DeletePlaneActivity.EXTRA_ICAO, plane.icao24)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}