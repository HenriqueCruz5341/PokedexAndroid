package com.example.pokedex.ui.recycleView.pokemon

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.LoadingLineBinding

class LoadingViewHolder(private val binding: LoadingLineBinding) : RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): LoadingLineBinding {
        return binding
    }

    fun bindVH() {
        binding.container.visibility = View.INVISIBLE
    }
}