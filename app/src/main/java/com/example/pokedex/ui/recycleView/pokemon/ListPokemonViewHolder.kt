package com.example.pokedex.ui.recycleView.pokemon

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.utils.ImageURL

class ListPokemonViewHolder(private val binding: PokemonLineBinding, private val listener: OnPokemonListener) : RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): PokemonLineBinding {
        return binding
    }

    fun bindVH(pokemon: PokemonPageableEntity) {
        ImageURL.loadImageBitmap(pokemon.image, object : ImageURL.OnImageLoaded {
            override fun run(value: Bitmap) {
                binding.pokemonImage.setImageBitmap(value)
            }
        })
        binding.pokemonName.text = pokemon.name

        binding.pokemonName.setOnClickListener {
            listener.onClick(pokemon)
        }
        binding.pokemonImage.setOnClickListener {
            listener.onClick(pokemon)
        }
    }
}