package com.example.pokedex.ui.recycleView.typeRelations

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.TypeRelationLineBinding
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO

class ListTypeRelationViewHolder(private val binding: TypeRelationLineBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(typeMultiplier: TypeMultiplierDTO, name: String, color: Int){
        binding.typeName.text = name
        binding.multiplier.text = "x${typeMultiplier.multiplier.toString()}"
        binding.typeName.setBackgroundColor(color)
    }

}