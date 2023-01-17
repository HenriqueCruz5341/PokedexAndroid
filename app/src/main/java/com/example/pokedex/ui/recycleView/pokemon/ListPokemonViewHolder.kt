package com.example.pokedex.ui.recycleView.pokemon

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import java.net.URL
import java.util.concurrent.Executors

class ListPokemonViewHolder(private val binding: PokemonLineBinding, private val listener: OnPokemonListener) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(pokemon: PokemonPageableEntity){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val stream = URL(pokemon.image).openStream()
                val image = BitmapFactory.decodeStream(stream)

                handler.post {
                    binding.pokemonImage.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.pokemonName.text = pokemon.name

        binding.pokemonName.setOnClickListener {
            listener.onClick(pokemon)
        }
        binding.pokemonImage.setOnClickListener {
            listener.onClick(pokemon)
        }
    }

}