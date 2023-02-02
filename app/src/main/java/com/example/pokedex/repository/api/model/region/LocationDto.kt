package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.google.gson.annotations.SerializedName

/**
 * The information when requesting a location.
 *
 * This class is used to parse the JSON response from the API when requesting a location.
 *
 * @property areas The list of locations areas on this location.
 * @property id The location unique identifier.
 * @property name The location name.
 */
class LocationDto {
    @SerializedName("areas")
    var areas: List<PageableItemDto> = listOf()

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""
}