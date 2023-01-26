package com.example.pokedex.repository.api.model.pokemon

import com.google.gson.annotations.SerializedName

class StatDto {
    @SerializedName("base_stat")
    var baseStat: Int = 0

    @SerializedName("stat")
    var name: NameDto = NameDto()
}