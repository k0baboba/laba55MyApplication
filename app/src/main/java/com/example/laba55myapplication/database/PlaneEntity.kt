package com.example.laba55myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.laba55myapplication.StateVector
import java.io.Serializable

@Entity(tableName = "planes_table")
data class PlaneEntity(
    @PrimaryKey
    val icao24: String,
    val callsign: String?,
    val originCountry: String,
    val velocity: Double? = 0.0,
    val baroAltitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val latitude: Double? = 0.0,
    val trueTrack: Double? = 0.0,
    val verticalRate: Double? = 0.0,
    val onGround: Boolean = false
) : Serializable {

    // превращения Entity из базы данных в объект для экрана Деталей
    fun toStateVector(): StateVector {
        return StateVector(
            icao24 = this.icao24,
            callsign = this.callsign,
            originCountry = this.originCountry,
            longitude = this.longitude,
            latitude = this.latitude,
            baroAltitude = this.baroAltitude,
            velocity = this.velocity,
            trueTrack = this.trueTrack,
            verticalRate = this.verticalRate,
            onGround = this.onGround
        )
    }
}