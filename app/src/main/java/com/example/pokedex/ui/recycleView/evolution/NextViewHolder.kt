package com.example.pokedex.ui.recycleView.evolution

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.NextLineBinding

/**
 * NextViewHolder is the class that will be used to bind the data to the view
 *
 * Since this view holder is used to show the next evolution, that is an static icon, it doesn't
 * need to bind any data to the view, so this class is empty.
 *
 * @param binding The binding that will be used to bind the data to the view
 */
class NextViewHolder(private val binding: NextLineBinding) : RecyclerView.ViewHolder(binding.root) {
}