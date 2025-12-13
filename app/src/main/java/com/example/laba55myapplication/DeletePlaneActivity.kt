package com.example.laba55myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import com.example.laba55myapplication.databinding.ActivityDeletePlaneBinding
import kotlinx.coroutines.launch

class DeletePlaneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeletePlaneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletePlaneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val icao = intent.getStringExtra(EXTRA_ICAO) ?: run {
            finish()
            return
        }

        binding.tvPlaneInfo.text = getString(R.string.delete_plane_confirmation, icao)

        binding.btnConfirmDelete.setOnClickListener {
            deletePlane(icao)
        }

        binding.btnCancelDelete.setOnClickListener {
            finish()
        }
    }

    private fun deletePlane(icao: String) {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val planeToDelete = PlaneEntity(icao, "", "", 0.0, 0.0)
            db.planeDao().deletePlane(planeToDelete)
            finish()
        }
    }

    companion object {
        const val EXTRA_ICAO = "com.example.laba55myapplication.EXTRA_ICAO_DELETE"
    }
}