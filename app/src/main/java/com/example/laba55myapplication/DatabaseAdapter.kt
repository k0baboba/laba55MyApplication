package com.example.laba55myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.laba55myapplication.database.PlaneEntity
import com.example.laba55myapplication.databinding.ItemPlaneBinding

class DatabaseAdapter(
    private val onItemClick: (PlaneEntity) -> Unit,
    private val onItemLongClick: (PlaneEntity) -> Unit
) : ListAdapter<PlaneEntity, DatabaseAdapter.PlaneViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaneViewHolder {
        val binding = ItemPlaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaneViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PlaneViewHolder(private val binding: ItemPlaneBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(getItem(position))
                }
                true
            }
        }

        fun bind(plane: PlaneEntity) {
            // Updated IDs to match item_plane.xml
            binding.callsign.text = plane.callsign ?: plane.icao24
            binding.originCountry.text = plane.originCountry
            binding.speed.text = "%.0f km/h".format((plane.velocity ?: 0.0) * 3.6)
            binding.altitude.text = "%.0f m".format(plane.baroAltitude ?: 0.0)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlaneEntity>() {
        override fun areItemsTheSame(oldItem: PlaneEntity, newItem: PlaneEntity) = oldItem.icao24 == newItem.icao24
        override fun areContentsTheSame(oldItem: PlaneEntity, newItem: PlaneEntity) = oldItem == newItem
    }
}