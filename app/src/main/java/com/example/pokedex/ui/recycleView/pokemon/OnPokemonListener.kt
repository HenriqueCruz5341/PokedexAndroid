package com.example.pokedex.ui.recycleView.pokemon

import com.example.pokedex.repository.database.model.PokemonPageableEntity

interface OnPokemonListener {
    fun onClick(pokemon: PokemonPageableEntity)
}