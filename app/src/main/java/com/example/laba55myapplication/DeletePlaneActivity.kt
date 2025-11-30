package com.example.laba55myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import kotlinx.coroutines.launch

class DeletePlaneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_plane) // Используем полноценный layout
        
        val icao = intent.getStringExtra(EXTRA_ICAO) ?: run {
            finish()
            return
        }

        val tvInfo = findViewById<TextView>(R.id.tv_plane_info)
        tvInfo.text = "Are you sure you want to permanently delete the record for plane with ICAO: $icao?"

        findViewById<Button>(R.id.btn_confirm_delete).setOnClickListener {
            deletePlane(icao)
        }

        findViewById<Button>(R.id.btn_cancel_delete).setOnClickListener {
            finish()
        }
    }

    private fun deletePlane(icao: String) {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            // Для удаления достаточно объекта только с ID
            val planeToDelete = PlaneEntity(icao, "", "", 0.0, 0.0)
            db.planeDao().deletePlane(planeToDelete)
            finish()
        }
    }

    companion object {
        const val EXTRA_ICAO = "com.example.laba55myapplication.EXTRA_ICAO_DELETE"
    }
}