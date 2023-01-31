package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

/**
 * The information of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon.
 *
 * @property id The id of the pokemon.
 * @property name The name of the pokemon.
 * @property species The species of the pokemon.
 * @property sprites The sprites of the pokemon.
 * @property stats The stats of the pokemon.
 * @property types The types of the pokemon.
 */
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