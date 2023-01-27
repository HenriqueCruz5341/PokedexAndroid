package com.example.pokedex.ui.recycleView.types

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.TypeLineBinding
import com.example.pokedex.repository.database.model.TypeEntity

class ListTypesViewHolder(private val binding: TypeLineBinding, private val listener: OnTypeListener) : RecyclerView.ViewHolder(binding.root) {
    fun bindVH(type: TypeEntity, name: String, color: Int){
        binding.typeName.text = name

        binding.typeName.setOnClickListener {
            listener.onClick(type)
        }
        binding.typeName.setBackgroundColor(color)
    }

    fun select(type: TypeEntity) {
        binding.typeItem.setBackgroundColor(Color.BLACK)
    }

    fun deselect(type: TypeEntity) {
        binding.typeItem.setBackgroundColor(Color.TRANSPARENT)
    }

}