package com.example.pokedex.ui.recycleView.region

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.RegionLineBinding
import com.example.pokedex.databinding.TypeLineBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.database.model.TypeEntity

class ListRegionViewHolder(private val binding: RegionLineBinding, private val listener: OnRegionListener) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(region: PageableItemDto){
        binding.regionName.text = region.name

        binding.regionName.setOnClickListener {
            listener.onClick(region)
        }
    }

}