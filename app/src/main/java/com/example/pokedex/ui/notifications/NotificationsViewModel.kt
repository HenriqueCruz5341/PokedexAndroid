package com.example.pokedex.ui.notifications

import android.graphics.pdf.PdfDocument.Page
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel : ViewModel() {

    var regionPageableItemList = MutableLiveData<List<String>>()

    fun getRegions(): LiveData<List<String>> {
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
                    val regionNamesList: MutableList<String> = mutableListOf()
                    resp.results.forEach {
                        regionNamesList.add(it.name)
                    }
                    regionPageableItemList.value = regionNamesList.toList()
                }
            }

            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
                //requestPokemonDatabase(pokemonId)
                println("FAIL")
            }
        })
    }
}