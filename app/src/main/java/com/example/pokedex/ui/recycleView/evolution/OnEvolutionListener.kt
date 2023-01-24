package com.example.pokedex.ui.recycleView.evolution

import com.example.pokedex.repository.database.model.EvolutionEntity

interface OnEvolutionListener {
    fun onClick(evolution: EvolutionEntity)
}