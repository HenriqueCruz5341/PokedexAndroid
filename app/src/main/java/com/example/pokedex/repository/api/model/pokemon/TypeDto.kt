package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

/**
 * The type of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon.
 *
 * @property name The name of the type.
 * @property url The url of the type.
 */
class TypeDto {
    @SerializedName("type")
    var name: NameDto = NameDto()

    @SerializedName("url")
    var url: String = ""
}