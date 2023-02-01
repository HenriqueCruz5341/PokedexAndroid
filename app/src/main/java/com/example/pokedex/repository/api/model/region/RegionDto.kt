package com.example.pokedex.repository.api.model.region

import com.example.pokedex.repository.api.model.PageableItemDto
import com.google.gson.annotations.SerializedName

/**
 * The information when requesting a region.
 *
 * This class is used to parse the JSON response from the API when requesting a region.
 *
 * @property locations The list of locations on this region.
 * @property id The region unique identifier.
 * @property name The region name.
 */
class RegionDto {
    @SerializedName("locations")
    var locations: List<PageableItemDto> = listOf()

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""
}