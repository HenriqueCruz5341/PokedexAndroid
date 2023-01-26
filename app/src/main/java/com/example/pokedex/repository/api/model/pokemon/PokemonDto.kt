package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

class PokemonDto {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("species")
    var species: SpecieBasicDto = SpecieBasicDto()

    @SerializedName("sprites")
    var sprites: SpriteDto = SpriteDto()

    @SerializedName("stats")
    var stats: List<StatDto> = listOf()

    @SerializedName("types")
    var types: List<TypeDto> = listOf()
}