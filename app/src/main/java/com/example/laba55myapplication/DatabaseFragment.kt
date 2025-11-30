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
            // Короткое нажатие теперь ничего не делает (или можно показывать подсказку "Long press for actions")
            onItemClick = { },
            
            // Долгое нажатие - единственное действие для вызова меню
            onItemLongClick = { plane -> showActionDialog(plane) }
        )

        binding.recyclerViewDb.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDb.adapter = adapter

        planeDao.getAllPlanes().observe(viewLifecycleOwner) { planes ->
            adapter.submitList(planes)
        }

        binding.fabAddPlane.setOnClickListener {
            val intent = Intent(requireContext(), AddEditPlaneActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showActionDialog(plane: PlaneEntity) {
        val options = arrayOf("View Details", "Update", "Delete")

        AlertDialog.Builder(requireContext())
            .setTitle("Actions for ${plane.icao24}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showDetails(plane) // Просмотр сразу открывает детали (это безопасно)
                    1 -> confirmUpdate(plane) // Перед обновлением спрашиваем
                    2 -> deletePlane(plane)   // Удаление уже имеет свое Activity с подтверждением
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

    private fun confirmUpdate(plane: PlaneEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Update Plane")
            .setMessage("Do you want to edit details for ${plane.icao24}?")
            .setPositiveButton("Yes") { _, _ ->
                updatePlane(plane)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun updatePlane(plane: PlaneEntity) {
        val intent = Intent(requireContext(), AddEditPlaneActivity::class.java).apply {
            putExtra(AddEditPlaneActivity.EXTRA_ICAO, plane.icao24)
            putExtra(AddEditPlaneActivity.EXTRA_CALLSIGN, plane.callsign)
            putExtra(AddEditPlaneActivity.EXTRA_COUNTRY, plane.originCountry)
            putExtra(AddEditPlaneActivity.EXTRA_VELOCITY, plane.velocity)
            putExtra(AddEditPlaneActivity.EXTRA_ALTITUDE, plane.baroAltitude)
            
            // Передаем новые поля
            putExtra(AddEditPlaneActivity.EXTRA_LONGITUDE, plane.longitude)
            putExtra(AddEditPlaneActivity.EXTRA_LATITUDE, plane.latitude)
            putExtra(AddEditPlaneActivity.EXTRA_TRUE_TRACK, plane.trueTrack)
            putExtra(AddEditPlaneActivity.EXTRA_ON_GROUND, plane.onGround)
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