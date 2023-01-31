package com.example.pokedex.repository.api.model.pokemonSpecie

import com.google.gson.annotations.SerializedName

/**
 * The information of a pokemon specie.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon specie.
 *
 * @property id The id of the pokemon specie.
 * @property name The name of the pokemon specie.
 * @property evolutionChain The basic information of an evolution chain of the pokemon specie.
 * @property genderRate The gender rate of the pokemon specie.
 * If -1 genderless, 0 100% male, 8 100% female, else is the rate of female.
 * @property varieties The list of varieties of the pokemon specie.
 */
class PokemonSpecieDto {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("evolution_chain")
    var evolutionChain: EvolutionChainBasicDto = EvolutionChainBasicDto()

    @SerializedName("gender_rate")
    var genderRate: Int = 0

    @SerializedName("varieties")
    var varieties: List<VarietyDto> = listOf()
}