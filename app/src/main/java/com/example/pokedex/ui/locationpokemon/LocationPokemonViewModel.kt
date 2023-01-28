package com.example.pokedex.ui.locationpokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.region.LocationAreaDto
import com.example.pokedex.repository.api.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationPokemonViewModel : ViewModel() {

    var pokemonBasicList = MutableLiveData<List<PageableItemDto>>()

    fun getPokemon(): LiveData<List<PageableItemDto>> {
        return pokemonBasicList
    }

    fun loadLocationArea(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<LocationAreaDto> = apiPokeService.getLocationAreaById(id)
        pokeApi.enqueue(object : Callback<LocationAreaDto> {
            override fun onResponse(
                call: Call<LocationAreaDto>,
                response: Response<LocationAreaDto>,
            ) {
                if(response.body() != null) {
                    val resp = response.body() as LocationAreaDto
                    println(resp.encounters)
                    pokemonBasicList.value = resp.encounters
                }
            }

            override fun onFailure(call: Call<LocationAreaDto>, t: Throwable) {
                // TODO get from database
                //requestPokemonDatabase(pokemonId)
                println("FAIL")
            }
        })
    }
}