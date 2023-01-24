package com.example.pokedex.repository.api.model.evolution

import com.google.gson.annotations.SerializedName

class SpeciesBasicDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}
