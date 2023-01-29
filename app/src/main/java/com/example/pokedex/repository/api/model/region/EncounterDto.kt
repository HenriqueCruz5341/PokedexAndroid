package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonBasicDto
import com.google.gson.annotations.SerializedName

class EncounterDto {
    @SerializedName("pokemon")
    var pokemon: PageableItemDto? = null
}