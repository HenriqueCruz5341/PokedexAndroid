package com.example.pokedex.repository.api.model

import com.google.gson.annotations.SerializedName

class PageableItemDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}