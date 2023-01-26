package com.example.pokedex.repository.api.model.evolution

import com.google.gson.annotations.SerializedName

class EvolvesToDto {
    @SerializedName("evolves_to")
    var evolvesTo: List<EvolvesToDto> = listOf()

    @SerializedName("species")
    var species: SpeciesBasicDto = SpeciesBasicDto()

}
