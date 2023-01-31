package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

/**
 * The name of some resource.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon.
 *
 * @property value The name of the resource.
 */
class NameDto {
    @SerializedName("name")
    var value: String = ""
}