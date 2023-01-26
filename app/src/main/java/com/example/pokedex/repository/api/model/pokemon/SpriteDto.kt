package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

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