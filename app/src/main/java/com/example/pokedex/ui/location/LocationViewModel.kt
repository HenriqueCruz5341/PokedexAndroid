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

    /**
     * This method returns a LiveData of the locationPageableItemList.
     *
     * @return LiveData of the locationPageableItemList, a List of PageableItemDto.
     */
    fun getLocations(): LiveData<List<PageableItemDto>> {
        return locationPageableItemList
    }

    /**
     * This method returns a LiveData of the filteredPageableItemList.
     *
     * @return LiveData of the filteredPageableItemList, a List of PageableItemDto.
     */
    fun getFiltered(): LiveData<List<PageableItemDto>> {
        return filteredPageableItemList
    }

    /**
     * This method remove all items of the filteredPageableItemList
     */
    fun removeFilter() {
        filteredPageableItemList.value = listOf()
    }

    /**
     * This method will filter the locationPageableItemList with a given string.
     *
     * The given string is searched in the locationPageableItemList, if one off the items contains
     * that string, it will be added to the filteredPageableItemList.
     *
     * @param name string to be searched in the locationPageableItemList.
     */
    fun filter(name: String) {
        val filterAuxList: MutableList<PageableItemDto> = mutableListOf()
        locationPageableItemList.value!!.forEach {
            val locationName = Converter.beautifyName(it.name)
            if(locationName.contains(name, ignoreCase = true)) filterAuxList.add(it)
        }
        filteredPageableItemList.value = filterAuxList.toList()
    }

    /**
     * This method load all locations from the API to the locationPageableItemList.
     *
     * The name loadRegion means that it load a region data by id. A Region contain many locations.
     */
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
                println("FAIL")
            }
        })
    }
}