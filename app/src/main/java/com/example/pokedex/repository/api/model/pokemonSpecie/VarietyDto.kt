package com.example.pokedex.repository.api.model.pokemonSpecie

import com.google.gson.annotations.SerializedName

class VarietyDto {
    @SerializedName("is_default")
    var isDefault: Boolean = false

    @SerializedName("pokemon")
    var pokemon: PokemonBasicDto = PokemonBasicDto()
}