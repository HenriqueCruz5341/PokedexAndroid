package com.example.pokedex.ui.recycleView.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.LoadingLineBinding
import com.example.pokedex.databinding.PokemonLineBinding
import com.example.pokedex.repository.database.model.PokemonPageableEntity

class ListPokemonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var pokemonList: MutableList<PokemonPageableEntity> = mutableListOf()
    private lateinit var listener: OnPokemonListener

    private val item: Int = 0
    private val loading: Int = 1

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

//        if(getItemViewType(position) == item){
//            println("É ITEM: $position")
//        }else
//            println("É LOADING: $position")

        if(getItemViewType(position) == item) {
            val pokemonVH: ListPokemonViewHolder = holder as ListPokemonViewHolder
            pokemonVH.getBinding().pokemonLine.visibility = View.VISIBLE
            pokemonVH.bindVH(pokemon)
        } else {
            val loadingVH: LoadingViewHolder = holder as LoadingViewHolder
            loadingVH.getBinding().container.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.count()
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == pokemonList.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    fun updatePokemonList(list: MutableList<PokemonPageableEntity>) {
        pokemonList = list
        notifyItemRangeChanged(0, list.size)
    }

    fun addAll(pokemons: MutableList<PokemonPageableEntity>) {
        for(pokemon in pokemons){
            add(pokemon)
        }
        notifyItemRangeInserted(pokemonList.size - 1, 30)
    }

    private fun add(pokemon: PokemonPageableEntity) {
        pokemonList.add(pokemon)
        //notifyItemInserted(pokemonList.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(PokemonPageableEntity())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position: Int = pokemonList.size -1

        pokemonList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setListener(pokemonListener: OnPokemonListener) {
        listener = pokemonListener
    }


}