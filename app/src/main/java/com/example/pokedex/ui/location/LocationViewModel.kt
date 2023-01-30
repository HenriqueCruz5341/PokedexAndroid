package com.example.pokedex.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.region.RegionDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    var locationPageableItemList = MutableLiveData<List<PageableItemDto>>()
    var filteredPageableItemList = MutableLiveData<List<PageableItemDto>>()

    fun getLocations(): LiveData<List<PageableItemDto>> {
        return locationPageableItemList
    }

    fun getFiltered(): LiveData<List<PageableItemDto>> {
        return filteredPageableItemList
    }

    fun removeFilter() {
        filteredPageableItemList.value = listOf()
    }

    fun filter(name: String) {
        val filterAuxList: MutableList<PageableItemDto> = mutableListOf()
        locationPageableItemList.value!!.forEach {
            val locationName = Converter.beautifyName(it.name)
            if(locationName.contains(name, ignoreCase = true)) filterAuxList.add(it)
        }
        filteredPageableItemList.value = filterAuxList.toList()
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