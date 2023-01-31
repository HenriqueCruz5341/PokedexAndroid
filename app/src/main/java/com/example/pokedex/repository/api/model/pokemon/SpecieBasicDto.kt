package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

/**
 * The basic information of a pokemon species.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon.
 *
 * @property name The name of the species.
 * @property url The url of the species.
 */
class SpecieBasicDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}