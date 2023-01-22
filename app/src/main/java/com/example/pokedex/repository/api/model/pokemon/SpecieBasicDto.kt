package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

class SpecieBasicDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}