package com.example.pokedex.repository.api.service

import com.example.pokedex.repository.api.model.PageableDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    fun getPokemonPageable(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<PageableDto>

}