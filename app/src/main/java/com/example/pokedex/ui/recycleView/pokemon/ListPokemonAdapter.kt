package com.example.pokedex.ui.recycleView.pokemon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity

class ListPokemonAdapter : RecyclerView.Adapter<ListPokemonViewHolder>() {

    private var pokemonList: List<PokemonPageableEntity> = listOf()
    private lateinit var listener: OnPokemonListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPokemonViewHolder {
        val item = PokemonLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ListPokemonViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: ListPokemonViewHolder, position: Int) {
        holder.bindVH(pokemonList[position])
    }

    override fun getItemCount(): Int {
        return pokemonList.count()
    }

    fun updatePokemonList(list: List<PokemonPageableEntity>) {
        pokemonList = list
        notifyItemRangeChanged(0, list.size)
    }

    fun setListener(pokemonListener: OnPokemonListener) {
        listener = pokemonListener
    }

}