package com.example.pokedex.repository.api.model.pokemonSpecie

import com.google.gson.annotations.SerializedName

/**
 * The variety of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon specie.
 *
 * @property isDefault If this is the default variety of the pokemon.
 * @property pokemon The basic information of the pokemon.
 */
class VarietyDto {
    @SerializedName("is_default")
    var isDefault: Boolean = false

    @SerializedName("pokemon")
    var pokemon: PokemonBasicDto = PokemonBasicDto()
}