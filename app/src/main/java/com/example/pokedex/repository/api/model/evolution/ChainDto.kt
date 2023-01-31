package com.example.pokedex.repository.api.model.evolution

import com.google.gson.annotations.SerializedName

/**
 * The chain of evolutions of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a evolution chain.
 *
 * @property evolvesTo The list of evolutions of the pokemon.
 * @property species The species of the pokemon.
 */
class ChainDto {
    @SerializedName("evolves_to")
    var evolvesTo: List<EvolvesToDto> = listOf()

    @SerializedName("species")
    var species: SpeciesBasicDto = SpeciesBasicDto()

}
