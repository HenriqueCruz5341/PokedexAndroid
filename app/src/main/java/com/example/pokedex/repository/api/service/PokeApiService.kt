package com.example.pokedex.repository.api.service

import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.evolution.EvolutionChainDto
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonSpecieDto
import com.example.pokedex.repository.api.model.region.LocationAreaDto
import com.example.pokedex.repository.api.model.region.LocationDto
import com.example.pokedex.repository.api.model.region.RegionDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The interface of the PokeApi.
 *
 * This interface is used to make the requests to the PokeApi. Each method is a request to a specific
 * endpoint of the API.
 */
interface PokeApiService {

    /**
     * Get a page of pokemons.
     * @param offset The offset of the page.
     * @param limit The limit of the page.
     */
    @GET("pokemon")
    fun getPokemonPageable(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<PageableDto>

    /**
     * Get a pokemon by its name or id.
     * @param name The name or id of the pokemon.
     */
    @GET("pokemon/{name}")
    fun getPokemonByNameOrId(@Path("name") name: String): Call<PokemonDto>

    /**
     * Get a pokemon by its id.
     * @param id The id of the pokemon.
     */
    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: Int): Call<PokemonDto>

    /**
     * Get a pokemon specie by its id.
     * @param id The id of the pokemon specie.
     */
    @GET("pokemon-species/{id}")
    fun getPokemonSpecieById(@Path("id") id: Int): Call<PokemonSpecieDto>

    /**
     * Get an evolution chain by its id.
     * @param id The id of the evolution chain.
     */
    @GET("evolution-chain/{id}")
    fun getEvolutionChainById(@Path("id") id: Int): Call<EvolutionChainDto>

    /**
     * Get a page of regions, as there aren't many regions all are taken at once.
     */
    @GET("region")
    fun getRegionPageable(): Call<PageableDto>

    /**
     * Get a region by its id.
     * @param id The id of the region.
     */
    @GET("region/{id}")
    fun getRegionById(@Path("id") id: Int): Call<RegionDto>

    /**
     * Get a location by its id.
     * @param id The id of the location.
     */
    @GET("location/{id}")
    fun getLocationById(@Path("id") id: Int): Call<LocationDto>

    /**
     * Get a location area by its id.
     * @param id The id of the location area.
     */
    @GET("location-area/{id}")
    fun getLocationAreaById(@Path("id") id: Int): Call<LocationAreaDto>
}