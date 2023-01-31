package com.example.pokedex.repository.api.model.evolution

import com.google.gson.annotations.SerializedName

/**
 * The basic information of a species.
 *
 * This class is used to parse the JSON response from the API when requesting a evolution chain.
 *
 * @property name The name of the species.
 * @property url The url of the species.
 */
class SpeciesBasicDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}
