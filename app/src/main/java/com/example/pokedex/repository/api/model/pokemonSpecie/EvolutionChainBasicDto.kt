package com.example.pokedex.repository.api.model.pokemonSpecie

import com.google.gson.annotations.SerializedName

/**
 * The basic information of an evolution chain.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon specie.
 *
 * @property url The url of the evolution chain.
 */
class EvolutionChainBasicDto {
    @SerializedName("url")
    var url: String = ""
}