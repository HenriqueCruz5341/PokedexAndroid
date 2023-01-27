package com.example.pokedex.ui.recycleView.region

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.RegionLineBinding
import com.example.pokedex.databinding.TypeLineBinding
import com.example.pokedex.repository.database.model.TypeEntity

class ListRegionViewHolder(private val binding: RegionLineBinding, private val listener: OnRegionListener) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(name: String){
        binding.regionName.text = name

        binding.regionName.setOnClickListener {
            listener.onClick(name)
        }
    }

}