package com.example.pokedex.ui.recycleView.typeRelations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.TypeRelationLineBinding
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO

class ListTypeRelationAdapter : RecyclerView.Adapter<ListTypeRelationViewHolder>() {

    private var typeMultiplierList: List<TypeMultiplierDTO> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTypeRelationViewHolder {
        val item = TypeRelationLineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ListTypeRelationViewHolder(item)
    }

    override fun onBindViewHolder(holder: ListTypeRelationViewHolder, position: Int) {
        holder.bindVH(typeMultiplierList[position])
    }

    override fun getItemCount(): Int {
        return typeMultiplierList.count()
    }

    fun updateTypeList(list: List<TypeMultiplierDTO>) {
        typeMultiplierList = list
        notifyDataSetChanged()
    }
}