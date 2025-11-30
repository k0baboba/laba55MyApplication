package com.example.laba55myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import com.example.laba55myapplication.databinding.ActivityAddEditPlaneBinding
import kotlinx.coroutines.launch

class UpdatePlaneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditPlaneBinding
    private val db by lazy { AppDatabase.getDatabase(this) }
    
    // Для обновления нам обязательно нужен ID
    private var planeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Мы переиспользуем layout, так как форма та же самая
        binding = ActivityAddEditPlaneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Update Plane Details"
        binding.buttonSave.text = "Update Record"
        
        // Получаем данные для редактирования
        if (intent.hasExtra(EXTRA_ICAO)) {
            planeId = intent.getStringExtra(EXTRA_ICAO)
            binding.editIcao.setText(planeId)
            binding.editIcao.isEnabled = false // Ключ менять нельзя
            
            binding.editCallsign.setText(intent.getStringExtra(EXTRA_CALLSIGN))
            binding.editCountry.setText(intent.getStringExtra(EXTRA_COUNTRY))
            binding.editVelocity.setText(intent.getDoubleExtra(EXTRA_VELOCITY, 0.0).toString())
            binding.editAltitude.setText(intent.getDoubleExtra(EXTRA_ALTITUDE, 0.0).toString())
            
            binding.editLongitude.setText(intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0).toString())
            binding.editLatitude.setText(intent.getDoubleExtra(EXTRA_LATITUDE, 0.0).toString())
            binding.editTrueTrack.setText(intent.getDoubleExtra(EXTRA_TRUE_TRACK, 0.0).toString())
            binding.checkboxOnGround.isChecked = intent.getBooleanExtra(EXTRA_ON_GROUND, false)
        } else {
            // Если ID не передан, мы не можем обновлять
            Toast.makeText(this, "Error: No plane ID provided", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.buttonSave.setOnClickListener {
            updatePlane()
        }
    }

    private fun updatePlane() {
        val icao = binding.editIcao.text.toString() // Оно заблокировано, но текст там есть
        val callsign = binding.editCallsign.text.toString()
        val country = binding.editCountry.text.toString()
        val velocity = binding.editVelocity.text.toString().toDoubleOrNull() ?: 0.0
        val altitude = binding.editAltitude.text.toString().toDoubleOrNull() ?: 0.0
        
        val longitude = binding.editLongitude.text.toString().toDoubleOrNull() ?: 0.0
        val latitude = binding.editLatitude.text.toString().toDoubleOrNull() ?: 0.0
        val trueTrack = binding.editTrueTrack.text.toString().toDoubleOrNull() ?: 0.0
        val onGround = binding.checkboxOnGround.isChecked

        if (country.isBlank()) {
            Toast.makeText(this, "Please enter Country", Toast.LENGTH_SHORT).show()
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
            db.planeDao().updatePlane(plane) // Используем update вместо insert
            finish()
        }
    }

    companion object {
        const val EXTRA_ICAO = "com.example.laba55myapplication.EXTRA_ICAO"
        const val EXTRA_CALLSIGN = "com.example.laba55myapplication.EXTRA_CALLSIGN"
        const val EXTRA_COUNTRY = "com.example.laba55myapplication.EXTRA_COUNTRY"
        const val EXTRA_VELOCITY = "com.example.laba55myapplication.EXTRA_VELOCITY"
        const val EXTRA_ALTITUDE = "com.example.laba55myapplication.EXTRA_ALTITUDE"
        const val EXTRA_LONGITUDE = "com.example.laba55myapplication.EXTRA_LONGITUDE"
        const val EXTRA_LATITUDE = "com.example.laba55myapplication.EXTRA_LATITUDE"
        const val EXTRA_TRUE_TRACK = "com.example.laba55myapplication.EXTRA_TRUE_TRACK"
        const val EXTRA_ON_GROUND = "com.example.laba55myapplication.EXTRA_ON_GROUND"
    }
}