package com.example.pokedex.ui.locationarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.region.LocationDto
import com.example.pokedex.repository.api.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationAreaViewModel : ViewModel() {

    var locationAreaPageableItemList = MutableLiveData<List<PageableItemDto>>()

    fun getLocationAreas(): LiveData<List<PageableItemDto>> {
        return locationAreaPageableItemList
    }

    fun loadLocation(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<LocationDto> = apiPokeService.getLocationById(id)
        pokeApi.enqueue(object : Callback<LocationDto> {
            override fun onResponse(
                call: Call<LocationDto>,
                response: Response<LocationDto>,
            ) {
                if(response.body() != null) {
                    val resp = response.body() as LocationDto
                    locationAreaPageableItemList.value = resp.areas
                }
            }

            override fun onFailure(call: Call<LocationDto>, t: Throwable) {
                // TODO get from database
                //requestPokemonDatabase(pokemonId)
                println("FAIL")
            }
        })
    }
}