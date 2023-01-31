package com.example.pokedex.repository.api.model.evolution

import com.google.gson.annotations.SerializedName

/**
 * The chain of evolutions of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a evolution chain.
 *
 * @property id The id of the evolution chain.
 * @property chain The chain of evolutions.
 */
class EvolutionChainDto {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("chain")
    var chain: ChainDto = ChainDto()
}