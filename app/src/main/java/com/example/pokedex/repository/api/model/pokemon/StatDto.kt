package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

/**
 * The stat of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon.
 *
 * @property baseStat The base stat of the pokemon.
 * @property name The name of the stat.
 */
class StatDto {
    @SerializedName("base_stat")
    var baseStat: Int = 0

    @SerializedName("stat")
    var name: NameDto = NameDto()
}