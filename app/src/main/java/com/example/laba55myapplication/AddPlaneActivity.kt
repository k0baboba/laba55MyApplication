package com.example.laba55myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import com.example.laba55myapplication.databinding.ActivityAddEditPlaneBinding
import kotlinx.coroutines.launch

class AddPlaneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPlaneBinding
    private val db by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPlaneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Add New Plane"
        binding.buttonSave.text = "Save to Database"

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
            // Проверяем, нет ли уже такого самолета (хотя Room replace может сделать это автоматом,
            // но для чистоты эксперимента добавим новый)
            db.planeDao().insertPlane(plane)
            finish()
        }
    }
}