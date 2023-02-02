package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonBasicDto
import com.google.gson.annotations.SerializedName

/**
 * The information of a pokemon encounter.
 *
 * This class is used to parse the JSON of a location area to get all encounter pokemon.
 *
 * @property pokemon The pokemon that can be encountered as a PageableItemDto.
 */
class EncounterDto {
    @SerializedName("pokemon")
    var pokemon: PageableItemDto? = null
}