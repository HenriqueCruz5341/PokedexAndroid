package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

class TypeDto {
    @SerializedName("type")
    var name: NameDto = NameDto()

    @SerializedName("url")
    var url: String = ""
}