package com.example.laba55myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class PlanesAdapter(private var states: List<StateVector>) : RecyclerView.Adapter<PlanesAdapter.PlaneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plane, parent, false)
        return PlaneViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaneViewHolder, position: Int) {
        val state = states[position]
        holder.callsign.text = state.getDisplayCallsign()
        holder.originCountry.text = state.originCountry
        holder.speed.text = state.getSpeedKmh()
        holder.altitude.text = state.getAltitudeMeters()

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("stateVector", state)
            }
            it.findNavController().navigate(R.id.action_planesFragment_to_planeDetailFragment, bundle)
        }
    }

    override fun getItemCount() = states.size

    fun updateData(newStates: List<StateVector>) {
        states = newStates
        notifyDataSetChanged()
    }

    class PlaneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callsign: TextView = itemView.findViewById(R.id.callsign)
        val originCountry: TextView = itemView.findViewById(R.id.origin_country)
        val speed: TextView = itemView.findViewById(R.id.speed)
        val altitude: TextView = itemView.findViewById(R.id.altitude)
    }
}