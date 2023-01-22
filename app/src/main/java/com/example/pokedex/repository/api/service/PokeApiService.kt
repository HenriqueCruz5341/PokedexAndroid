package com.example.pokedex.repository.api.service

import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    fun getPokemonPageable(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<PageableDto>

    @GET("pokemon/{name}")
    fun getPokemonByName(@Path("name") name: String): Call<PokemonDto>

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: Int): Call<PokemonDto>

}