package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.google.gson.annotations.SerializedName

class LocationDto {
    @SerializedName("areas")
    var areas: List<PageableItemDto> = listOf()

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""
}