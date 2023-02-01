package com.example.pokedex.ui.recycleView.pokemon

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.LoadingLineBinding

/**
 * LoadingViewHolder is the class that will be used to bind the data to the view
 *
 * The loading view holder is used to show a loading icon when the data is being loaded.
 */
class LoadingViewHolder(private val binding: LoadingLineBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     * This method binds the data to the view. This method hides the container view, so the loading
     * icon will not be shown.
     */
    fun bindVH() {
        binding.container.visibility = View.INVISIBLE
    }
}