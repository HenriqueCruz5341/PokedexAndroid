package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

class NameDto {
    @SerializedName("name")
    var value: String = ""
}