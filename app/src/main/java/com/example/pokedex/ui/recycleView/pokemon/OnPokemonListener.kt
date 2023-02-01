package com.example.pokedex.ui.recycleView.pokemon

import com.example.pokedex.repository.database.model.PokemonPageableEntity

/**
 * OnPokemonListener is the interface that will be used to handle the click on the pokemon
 * recycler view
 */
interface OnPokemonListener {

    /**
     * This method will be called when the user clicks on the pokemon
     *
     * @param pokemon The pokemon that was clicked
     */
    fun onClick(pokemon: PokemonPageableEntity)
}