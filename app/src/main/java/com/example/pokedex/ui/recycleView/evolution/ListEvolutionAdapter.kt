package com.example.pokedex.ui.recycleView.evolution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.EvolutionLineBinding
import com.example.pokedex.databinding.NextLineBinding
import com.example.pokedex.repository.database.model.EvolutionEntity

class ListEvolutionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var evolutionList: List<EvolutionEntity> = listOf()
    private lateinit var listener: OnEvolutionListener

    private val item: Int = 0
    private val next: Int = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == item) {
            val binding =
                EvolutionLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ListEvolutionViewHolder(binding, listener)
        } else {
            val binding =
                NextLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NextViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val evolution = evolutionList[position]

        if (getItemViewType(position) == item) {
            val evolutionVH: ListEvolutionViewHolder = holder as ListEvolutionViewHolder
            evolutionVH.bindVH(evolution)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (evolutionList[position].id == -1) next
        else item
    }

    override fun getItemCount(): Int {
        return evolutionList.count()
    }

    fun updateEvolutionList(list: List<EvolutionEntity>) {
        evolutionList = list
        notifyDataSetChanged()
    }

    fun setListener(evolutionListener: OnEvolutionListener) {
        listener = evolutionListener
    }
}