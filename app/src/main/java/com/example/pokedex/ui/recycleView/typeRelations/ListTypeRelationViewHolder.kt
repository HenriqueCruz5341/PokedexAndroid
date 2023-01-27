package com.example.pokedex.ui.recycleView.typeRelations

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.TypeRelationLineBinding
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO

class ListTypeRelationViewHolder(private val binding: TypeRelationLineBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(typeMultiplier: TypeMultiplierDTO, name: String, color: Int){
        val multiplierText = "x${typeMultiplier.multiplier}"

        binding.typeName.text = name
        binding.multiplier.text = multiplierText

        if (typeMultiplier.multiplier == 1f) {
            binding.multiplier.setBackgroundColor(Color.GRAY)
        }else if (typeMultiplier.multiplier <= 0f){
            binding.multiplier.setBackgroundColor(Color.BLACK)
        } else if (typeMultiplier.multiplier < 1f) {
            binding.multiplier.setBackgroundColor(Color.rgb(200, 38, 19))
        } else {
            binding.multiplier.setBackgroundColor(Color.rgb(111, 192, 64))
        }

        binding.typeName.setBackgroundColor(color)
    }

}