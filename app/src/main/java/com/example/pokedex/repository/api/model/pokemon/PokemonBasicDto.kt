package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

class PokemonBasicDto {
    @SerializedName("userId")
    var userID: Int = 0

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("title")
    var title: String = ""

    @SerializedName("body")
    var body: String = ""
}