package com.example.pokedex.repository.api.model.evolution

import com.google.gson.annotations.SerializedName

class EvolutionChainDto {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("chain")
    var chain: ChainDto = ChainDto()
}