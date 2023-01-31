package com.example.pokedex.repository.api.model.pokemonSpecie

import com.google.gson.annotations.SerializedName

/**
 * The basic information of a pokemon
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon specie.
 *
 * @property name The name of the pokemon.
 * @property url The url of the pokemon.
 */
class PokemonBasicDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}