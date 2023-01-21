package com.example.pokedex.ui.recycleView.typeRelations

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.TypeRelationLineBinding
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO

class ListTypeRelationViewHolder(private val binding: TypeRelationLineBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(typeMultiplier: TypeMultiplierDTO){
        binding.typeName.text = typeMultiplier.name
        binding.multiplier.text = "x${typeMultiplier.multiplier.toString()}"
    }

}