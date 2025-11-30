package com.example.laba55myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import com.example.laba55myapplication.databinding.ActivityAddEditPlaneBinding
import kotlinx.coroutines.launch

class AddEditPlaneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPlaneBinding
    private val db by lazy { AppDatabase.getDatabase(this) }
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPlaneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_ICAO)) {
            isEditMode = true
            binding.editIcao.setText(intent.getStringExtra(EXTRA_ICAO))
            binding.editIcao.isEnabled = false
            binding.editCallsign.setText(intent.getStringExtra(EXTRA_CALLSIGN))
            binding.editCountry.setText(intent.getStringExtra(EXTRA_COUNTRY))
            binding.editVelocity.setText(intent.getDoubleExtra(EXTRA_VELOCITY, 0.0).toString())
            binding.editAltitude.setText(intent.getDoubleExtra(EXTRA_ALTITUDE, 0.0).toString())
            
            // Новые поля
            binding.editLongitude.setText(intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0).toString())
            binding.editLatitude.setText(intent.getDoubleExtra(EXTRA_LATITUDE, 0.0).toString())
            binding.editTrueTrack.setText(intent.getDoubleExtra(EXTRA_TRUE_TRACK, 0.0).toString())
            binding.checkboxOnGround.isChecked = intent.getBooleanExtra(EXTRA_ON_GROUND, false)

            binding.buttonSave.text = "Update Plane"
            title = "Edit Plane"
        } else {
            title = "Add Plane"
        }

        binding.buttonSave.setOnClickListener {
            savePlane()
        }
    }

    private fun savePlane() {
        val icao = binding.editIcao.text.toString()
        val callsign = binding.editCallsign.text.toString()
        val country = binding.editCountry.text.toString()
        val velocity = binding.editVelocity.text.toString().toDoubleOrNull() ?: 0.0
        val altitude = binding.editAltitude.text.toString().toDoubleOrNull() ?: 0.0
        
        // Читаем новые поля
        val longitude = binding.editLongitude.text.toString().toDoubleOrNull() ?: 0.0
        val latitude = binding.editLatitude.text.toString().toDoubleOrNull() ?: 0.0
        val trueTrack = binding.editTrueTrack.text.toString().toDoubleOrNull() ?: 0.0
        val onGround = binding.checkboxOnGround.isChecked

        if (icao.isBlank() || country.isBlank()) {
            Toast.makeText(this, "Please enter ICAO and Country", Toast.LENGTH_SHORT).show()
            return
        }

        val plane = PlaneEntity(
            icao24 = icao,
            callsign = callsign,
            originCountry = country,
            velocity = velocity,
            baroAltitude = altitude,
            longitude = longitude,
            latitude = latitude,
            trueTrack = trueTrack,
            verticalRate = 0.0,
            onGround = onGround
        )

        lifecycleScope.launch {
            if (isEditMode) {
                db.planeDao().updatePlane(plane)
            } else {
                db.planeDao().insertPlane(plane)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_ICAO = "com.example.laba55myapplication.EXTRA_ICAO"
        const val EXTRA_CALLSIGN = "com.example.laba55myapplication.EXTRA_CALLSIGN"
        const val EXTRA_COUNTRY = "com.example.laba55myapplication.EXTRA_COUNTRY"
        const val EXTRA_VELOCITY = "com.example.laba55myapplication.EXTRA_VELOCITY"
        const val EXTRA_ALTITUDE = "com.example.laba55myapplication.EXTRA_ALTITUDE"
        
        // Новые константы для Intent
        const val EXTRA_LONGITUDE = "com.example.laba55myapplication.EXTRA_LONGITUDE"
        const val EXTRA_LATITUDE = "com.example.laba55myapplication.EXTRA_LATITUDE"
        const val EXTRA_TRUE_TRACK = "com.example.laba55myapplication.EXTRA_TRUE_TRACK"
        const val EXTRA_ON_GROUND = "com.example.laba55myapplication.EXTRA_ON_GROUND"
    }
}