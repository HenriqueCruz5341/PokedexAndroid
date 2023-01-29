package com.example.pokedex.ui.locationpokemon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.region.LocationAreaDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationPokemonViewModel(application: Application) : AndroidViewModel(application) {

    var pokemonPageableItemList = MutableLiveData<List<PokemonPageableEntity>>()

    fun getPokemon(): LiveData<List<PokemonPageableEntity>> {
        return pokemonPageableItemList
    }

    fun loadLocationArea(locationAreaId: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<LocationAreaDto> = apiPokeService.getLocationAreaById(locationAreaId)
        pokeApi.enqueue(object : Callback<LocationAreaDto> {
            override fun onResponse(
                call: Call<LocationAreaDto>,
                response: Response<LocationAreaDto>,
            ) {
                if(response.body() != null) {
                    val resp = response.body() as LocationAreaDto
                    val pokemonPageableList: MutableList<PokemonPageableEntity> = mutableListOf()
                    resp.encounters.forEach {
                        val pokemon = it.pokemon!!
                        val pokemonId = Converter.idFromUrl(pokemon.url)
                        pokemonPageableList.add(PokemonPageableEntity().apply {
                            id = pokemonId
                            name = pokemon.name
                            image = Converter.urlImageFromId(pokemonId)
                            url = pokemon.url
                            count = 0
                        })
                    }
                    pokemonPageableItemList.value = pokemonPageableList.toList()
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