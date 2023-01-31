package com.example.pokedex.repository.api.model

import com.google.gson.annotations.SerializedName

/**
 * The information about the item when requesting a list of items.
 *
 * This class is used to parse the JSON response from the API when requesting a list of items.
 *
 * @property name The name of the item.
 * @property url The url of the item.
 */
class PageableItemDto {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("url")
    var url: String = ""
}