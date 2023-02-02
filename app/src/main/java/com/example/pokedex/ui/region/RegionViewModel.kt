package com.example.pokedex.ui.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegionViewModel : ViewModel() {

    var regionPageableItemList = MutableLiveData<List<PageableItemDto>>()

    /**
     * This method returns a LiveData of the regionPageableItemList.
     *
     * @return LiveData of the regionPageableItemList, a List of PageableItemDto.
     */
    fun getRegions(): LiveData<List<PageableItemDto>> {
        return regionPageableItemList
    }

    /**
     * This method load all regions from the API to the regionPageableItemList.
     */
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
                println("FAIL")
            }
        })
    }
}