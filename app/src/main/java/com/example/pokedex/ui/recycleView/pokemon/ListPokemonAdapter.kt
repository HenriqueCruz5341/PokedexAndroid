package com.example.pokedex.ui.recycleView.pokemon

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.LoadingLineBinding
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity

class ListPokemonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var pokemonList: MutableList<PokemonPageableEntity> = mutableListOf()
    private lateinit var listener: OnPokemonListener

    private val loading: Int = -1
    private val spacing: Int = 0
    private val item: Int = 1

    private var isLoadingAdded: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding = PokemonLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ListPokemonViewHolder(binding, listener)
        }else{
            val binding= LoadingLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        if(getItemViewType(position) == item) {
            val pokemonVH: ListPokemonViewHolder = holder as ListPokemonViewHolder
            pokemonVH.bindVH(pokemon)
        } else {
            val loadingVH: LoadingViewHolder = holder as LoadingViewHolder
            if (getItemViewType(position) == spacing) loadingVH.bindVH()
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.count()
    }

    override fun getItemViewType(position: Int): Int {
        return if (pokemonList[position].id > 0) item else pokemonList[position].id
    }

    fun addAll(pokemons: MutableList<PokemonPageableEntity>) {
        if (pokemonList.isNotEmpty() && pokemons[0].id < pokemonList[pokemonList.size-3].id) return

        for(pokemon in pokemons){
            add(pokemon)
        }
    }

    private fun add(pokemon: PokemonPageableEntity) {
        pokemonList.add(pokemon)
        notifyItemInserted(pokemonList.size - 1)
    }

    fun addLoadingFooter() {
        if(!isLoadingAdded) {
            add(PokemonPageableEntity().apply { id = spacing; name = "Spacing"})
            add(PokemonPageableEntity().apply { id = loading; name = "Loading"})
            isLoadingAdded = true
        }
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            val positionLoading: Int = pokemonList.size - 1
            pokemonList.removeAt(positionLoading)
            notifyItemRemoved(positionLoading)

            val positionSpacing: Int = pokemonList.size - 1
            pokemonList.removeAt(positionSpacing)
            notifyItemRemoved(positionSpacing)
            isLoadingAdded = false
        }
    }

    fun setListener(pokemonListener: OnPokemonListener) {
        listener = pokemonListener
    }


}