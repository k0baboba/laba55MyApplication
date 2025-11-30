package com.example.laba55myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlaneDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plane_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Пытаемся получить данные по разным ключам (для совместимости)
        val state = arguments?.getParcelable<StateVector>("plane") 
                 ?: arguments?.getParcelable<StateVector>("stateVector")

        state?.let {
            view.findViewById<TextView>(R.id.detail_callsign).text = it.getDisplayCallsign()
            view.findViewById<TextView>(R.id.detail_icao).text = "ICAO24: ${it.icao24}"
            view.findViewById<TextView>(R.id.detail_origin_country).text = "Origin: ${it.originCountry}"
            
            view.findViewById<TextView>(R.id.detail_speed).text = "Speed: ${it.getSpeedKmh()}"
            view.findViewById<TextView>(R.id.detail_altitude).text = "Altitude: ${it.getAltitudeMeters()}"
            
            val lat = it.latitude ?: 0.0
            val lon = it.longitude ?: 0.0
            view.findViewById<TextView>(R.id.detail_coordinates).text = "Coordinates: $lat, $lon"
            
            view.findViewById<TextView>(R.id.detail_track).text = "True Track: ${it.trueTrack ?: 0}°"
            view.findViewById<TextView>(R.id.detail_vertical_rate).text = "Vertical Rate: ${it.verticalRate ?: 0} m/s"
            
            val status = if (it.onGround) "On Ground" else "In Air"
            view.findViewById<TextView>(R.id.detail_on_ground).text = "Status: $status"
        }
    }
}