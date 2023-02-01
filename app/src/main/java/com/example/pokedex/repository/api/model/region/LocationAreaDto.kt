package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonBasicDto
import com.google.gson.annotations.SerializedName

/**
 * The information when requesting a location area.
 *
 * This class is used to parse the JSON response from the API when requesting a location area.
 *
 * @property encounters The list of pokemon encounters on this location area.
 * @property id The location area unique identifier.
 * @property name The location area name.
 */
class LocationAreaDto {
    @SerializedName("pokemon_encounters")
    var encounters: List<EncounterDto> = listOf()

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""
}