package com.example.laba55myapplication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Ответ от API - states массив массивов
data class OpenSkyResponse(
    @SerializedName("time")
    val time: Long,

    @SerializedName("states")
    val states: List<List<Any?>>? // массив массивов
)

// хранение статистики
data class PlanesStatistics(
    val total: Int,
    val inAir: Int,
    val onGround: Int
)

// модель самолёта после обработки
@Parcelize // чтобы пересылать
data class StateVector(
    val icao24: String, // позывной самолёта
    val callsign: String?,
    val originCountry: String,
    val longitude: Double?,
    val latitude: Double?,
    val baroAltitude: Double?,
    val velocity: Double?,
    val trueTrack: Double?,
    val verticalRate: Double?,
    val onGround: Boolean
) : Parcelable { // реализация Parcelable
    companion object {
        fun fromArray(state: List<Any?>): StateVector? {
            return try {
                StateVector(
                    icao24 = state[0] as? String ?: return null,
                    callsign = (state[1] as? String)?.trim(),
                    originCountry = state[2] as? String ?: "Unknown",
                    longitude = state[5] as? Double,
                    latitude = state[6] as? Double,
                    baroAltitude = state[7] as? Double,
                    velocity = state[9] as? Double,
                    trueTrack = state[10] as? Double,
                    verticalRate = state[11] as? Double,
                    onGround = (state[8] as? Boolean) ?: false
                )
            } catch (e: Exception) {
                null // игнор невалидные данные
            }
        }
    }

    // вспомогательные методы для UI
    fun getSpeedKmh(): String {
        return velocity?.let {
            "%.0f км/ч".format(it * 3.6) // из м/с
        } ?: "N/A"
    }

    fun getAltitudeMeters(): String {
        return baroAltitude?.let {
            "%.0f м".format(it)
        } ?: "N/A"
    }

    fun getDisplayCallsign(): String {
        return callsign?.takeIf { it.isNotBlank() } ?: icao24
    }
}