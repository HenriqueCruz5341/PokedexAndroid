package com.example.pokedex.ui.locationpokemon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.region.EncounterDto
import com.example.pokedex.repository.api.model.region.LocationAreaDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationPokemonViewModel(application: Application) : AndroidViewModel(application) {

    var pokemonPageableItemList = MutableLiveData<List<PokemonPageableEntity>>()

    /**
     * This method returns a LiveData of the pokemonPageableItemList.
     *
     * @return LiveData of the pokemonPageableItemList, a List of PokemonPageableEntity.
     */
    fun getPokemon(): LiveData<List<PokemonPageableEntity>> {
        return pokemonPageableItemList
    }

    /**
     * This method load all location pokemon from the API to the pokemonPageableItemList.
     *
     * The name loadLocationArea means that it load a location area data by id. A Location Area
     * contain many pokemon.
     */
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
                        pokemonPageableList.add(convertEncounterDtoToPokemonPageableEntity(it))
                    }
                    pokemonPageableItemList.value = pokemonPageableList.toList()
                }
            }

            override fun onFailure(call: Call<LocationAreaDto>, t: Throwable) {
                println("FAIL")
            }
        })
    }

    /**
     * This method converts a EncounterDto to a PokemonPageableEntity
     *
     * @param pokemon the EncounterDto to be converted
     * @return the PokemonPageableEntity resultant
     */
    private fun convertEncounterDtoToPokemonPageableEntity(pokemon: EncounterDto): PokemonPageableEntity {
        val pokemonPageable = pokemon.pokemon!!
        val pokemonId = Converter.idFromUrl(pokemonPageable.url)
        return PokemonPageableEntity().apply {
            id = pokemonId
            name = Converter.beautifyName(pokemonPageable.name)
            image = Converter.urlImageFromId(pokemonId)
            url = pokemonPageable.url
            count = 0
        }
    }
}