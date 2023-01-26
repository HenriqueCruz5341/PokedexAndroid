package com.example.pokedex.ui.recycleView.pokemon

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.LoadingLineBinding

class LoadingViewHolder(private val binding: LoadingLineBinding) : RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): LoadingLineBinding {
        return binding
    }
}