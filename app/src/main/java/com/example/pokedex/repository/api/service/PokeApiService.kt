package com.example.pokedex.repository.api.service

import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.evolution.EvolutionChainDto
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonSpecieDto
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

    @GET("pokemon-species/{id}")
    fun getPokemonSpecieById(@Path("id") id: Int): Call<PokemonSpecieDto>

    @GET("evolution-chain/{id}")
    fun getEvolutionChainById(@Path("id") id: Int): Call<EvolutionChainDto>

    @GET("region")
    fun getRegionPageable(): Call<PageableDto>
}