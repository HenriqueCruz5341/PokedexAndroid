package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

/**
 * The sprites of a pokemon.
 *
 * This class is used to parse the JSON response from the API when requesting a pokemon.
 *
 * @property default The image url of the default sprite.
 * @property female THe image url of the female sprite. If null, male and female sprites are the same.
 * @property shiny The image url of the shiny sprite. If null, shiny and default sprites are the same.
 * @property shinyFemale The image url of the shiny female sprite. If null, shinyFemale and shiny sprites are the same.
 */
class SpriteDto {
    @SerializedName("front_default")
    var default: String = ""

    @SerializedName("front_female")
    var female: String? = null

    @SerializedName("front_shiny")
    var shiny: String? = null

    @SerializedName("front_shiny_female")
    var shinyFemale: String? = null
}