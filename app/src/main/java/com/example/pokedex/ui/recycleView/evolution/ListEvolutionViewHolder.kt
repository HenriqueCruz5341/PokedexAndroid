package com.example.pokedex.ui.recycleView.evolution

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.EvolutionLineBinding
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.utils.ImageURL

/**
 * ListEvolutionViewHolder is the class that will be used to bind the data to the view
 *
 * This class will be used to bind the data to the view and will be used by the adapter to show
 * the data in the recycler view.
 *
 * @param binding The binding that will be used to bind the data to the view
 * @param listener The listener that will be used to listen to the click events
 */
class ListEvolutionViewHolder(private val binding: EvolutionLineBinding, private val listener: OnEvolutionListener) : RecyclerView.ViewHolder(binding.root) {

    /**
     * This method binds the data and sets the click listener to the view. A particularity of this
     * method is that it uses the ImageURL class to load the image from the url and then set it to
     * the image view.
     *
     * @param evolution The evolution that will be used to bind the data to the view
     */
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