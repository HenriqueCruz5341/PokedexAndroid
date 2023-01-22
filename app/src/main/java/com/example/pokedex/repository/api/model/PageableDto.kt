package com.example.pokedex.repository.api.model

import com.google.gson.annotations.SerializedName

class PageableDto {
    @SerializedName("count")
    var count: Int = 0

    @SerializedName("next")
    var next: String = ""

    @SerializedName("previous")
    var previous: String? = null

    @SerializedName("results")
    var results: List<PageableItemDto> = listOf()
}