package com.example.pokedex.ui.recycleView.evolution

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.EvolutionLineBinding
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.utils.ImageURL

class ListEvolutionViewHolder(private val binding: EvolutionLineBinding, private val listener: OnEvolutionListener) : RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): EvolutionLineBinding {
        return binding
    }

    fun bindVH(evolution: EvolutionEntity) {
        ImageURL.loadImageBitmap(evolution.pokemonImage, object : ImageURL.OnImageLoaded {
            override fun run(value: Bitmap) {
                binding.pokemonImage.setImageBitmap(value)
            }
        })
        binding.pokemonName.text = evolution.pokemonName

        binding.pokemonName.setOnClickListener {
            listener.onClick(evolution)
        }
        binding.pokemonImage.setOnClickListener {
            listener.onClick(evolution)
        }
    }
}