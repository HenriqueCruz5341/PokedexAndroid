package com.example.pokedex.repository.api.model

import com.google.gson.annotations.SerializedName

/**
 * The information when requesting a pageable list.
 *
 * This class is used to parse the JSON response from the API when requesting some pageable list.
 *
 * @property count The total number of items.
 * @property next The url of the next page.
 * @property previous The url of the previous page. If there is no previous page, this value is null.
 * @property results The list of items.
 */
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