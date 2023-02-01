package com.example.pokedex.ui.recycleView.evolution

import com.example.pokedex.repository.database.model.EvolutionEntity

/**
 * OnEvolutionListener is the interface that will be used to handle the click on the evolution
 * recycler view
 */
interface OnEvolutionListener {

    /**
     * This method will be called when the user clicks on the evolution
     *
     * @param evolution The evolution that was clicked
     */
    fun onClick(evolution: EvolutionEntity)
}