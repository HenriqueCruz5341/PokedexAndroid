package com.example.pokedex.ui.recycleView.types

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.databinding.TypeLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.ui.recycleView.pokemon.OnPokemonListener
import java.net.URL
import java.util.concurrent.Executors

class ListTypesViewHolder(private val binding: TypeLineBinding, private val listener: OnTypeListener) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(type: TypeEntity){
        binding.typeName.text = type.name

        binding.typeName.setOnClickListener {
            listener.onClick(type)
        }
    }

}