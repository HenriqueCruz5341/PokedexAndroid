package com.example.pokedex.ui.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.region.RegionDto
import com.example.pokedex.repository.api.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegionViewModel : ViewModel() {

    var regionPageableItemList = MutableLiveData<List<PageableItemDto>>()

    fun getRegions(): LiveData<List<PageableItemDto>> {
        return regionPageableItemList
    }

    fun loadRegions() {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PageableDto> = apiPokeService.getRegionPageable()
        pokeApi.enqueue(object : Callback<PageableDto> {
            override fun onResponse(
                call: Call<PageableDto>,
                response: Response<PageableDto>,
            ) {
                if(response.body() != null) {
                    val resp = response.body() as PageableDto
                    regionPageableItemList.value = resp.results
                }
            }

            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
                // TODO get from database
                //requestPokemonDatabase(pokemonId)
                println("FAIL")
            }
        })
    }

    fun getRegion(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<RegionDto> = apiPokeService.getRegionById(id)
        pokeApi.enqueue(object : Callback<RegionDto> {
            override fun onResponse(
                call: Call<RegionDto>,
                response: Response<RegionDto>,
            ) {
                if(response.body() != null) {
                    val resp = response.body() as RegionDto
                    val locationsList: List<PageableItemDto> = resp.locations
                }
            }

            override fun onFailure(call: Call<RegionDto>, t: Throwable) {
                // TODO get from database
                //requestPokemonDatabase(pokemonId)
                println("FAIL")
            }
        })
    }
}