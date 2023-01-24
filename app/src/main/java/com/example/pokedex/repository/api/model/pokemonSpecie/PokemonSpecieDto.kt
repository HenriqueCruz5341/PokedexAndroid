package com.example.pokedex.repository.api.model.pokemonSpecie

import com.google.gson.annotations.SerializedName

class PokemonSpecieDto {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("evolution_chain")
    var evolutionChain: EvolutionChainBasicDto = EvolutionChainBasicDto()

    @SerializedName("varieties")
    var varieties: List<VarietyDto> = listOf()
}