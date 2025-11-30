package com.example.laba55myapplication

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.laba55myapplication.database.AppDatabase
import com.example.laba55myapplication.database.PlaneEntity
import kotlinx.coroutines.launch

class DeletePlaneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // У этого Activity нет своего layout, оно прозрачное.
        // Сразу показываем диалог подтверждения.

        val icao = intent.getStringExtra(EXTRA_ICAO)
        
        if (icao != null) {
            AlertDialog.Builder(this)
                .setTitle("Delete Plane")
                .setMessage("Are you sure you want to delete plane $icao?")
                .setPositiveButton("Delete") { _, _ ->
                    deletePlane(icao)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    finish()
                }
                .setOnDismissListener {
                    // Если пользователь нажал мимо диалога - закрываем Activity
                    if (!isFinishing) finish()
                }
                .show()
        } else {
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