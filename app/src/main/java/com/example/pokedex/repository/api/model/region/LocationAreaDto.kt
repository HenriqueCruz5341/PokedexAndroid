package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonBasicDto
import com.google.gson.annotations.SerializedName

class LocationAreaDto {
    @SerializedName("pokemon_encounters")
    var encounters: List<PageableItemDto> = listOf()

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""
}