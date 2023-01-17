package com.example.pokedex.ui.recycleView.types

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.TypeLineBinding
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.ui.recycleView.pokemon.OnPokemonListener

class ListTypesAdapter : RecyclerView.Adapter<ListTypesViewHolder>() {

    private var typeList: List<TypeEntity> = listOf()
    private lateinit var listener: OnTypeListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTypesViewHolder {
        val item = TypeLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ListTypesViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: ListTypesViewHolder, position: Int) {
        holder.bindVH(typeList[position])
    }

    override fun getItemCount(): Int {
        return typeList.count()
    }

    fun updateTypeList(list: List<TypeEntity>) {
        typeList = list
        notifyItemRangeChanged(0, list.size)
    }

    fun setListener(typeListener: OnTypeListener) {
        listener = typeListener
    }
}