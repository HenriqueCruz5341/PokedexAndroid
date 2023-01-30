package com.example.pokedex.ui.recycleView.pageableitem

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.RegionLineBinding
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.utils.Converter

class ListPageableItemViewHolder(private val binding: RegionLineBinding, private val listener: OnPageableItemListener) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(region: PageableItemDto){
        binding.regionName.text = Converter.beautifyName(region.name)

        binding.regionName.setOnClickListener {
            listener.onClick(region)
        }
    }

}