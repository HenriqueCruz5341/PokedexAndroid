package com.example.pokedex.ui.recycleView.types

import com.example.pokedex.repository.database.model.TypeEntity

interface OnTypeListener {
    fun onClick(type: TypeEntity)
}