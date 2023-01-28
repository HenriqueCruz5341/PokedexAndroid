package com.example.pokedex.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.region.RegionDto
import com.example.pokedex.repository.api.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    var locationPageableItemList = MutableLiveData<List<PageableItemDto>>()

    fun getLocations(): LiveData<List<PageableItemDto>> {
        return locationPageableItemList
    }

    fun loadRegion(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<RegionDto> = apiPokeService.getRegionById(id)
        pokeApi.enqueue(object : Callback<RegionDto> {
            override fun onResponse(
                call: Call<RegionDto>,
                response: Response<RegionDto>,
            ) {
                if(response.body() != null) {
                    val resp = response.body() as RegionDto
                    locationPageableItemList.value = resp.locations
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