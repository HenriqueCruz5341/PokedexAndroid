package com.example.pokedex.ui.home

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.model.PageableItemDto
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Converter
import com.example.pokedex.utils.StatusMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var pokemonList = MutableLiveData<List<PokemonPageableEntity>>()
    private var filteredPokemonList = MutableLiveData<List<PokemonPageableEntity>>()
    private var newPokemonList = mutableListOf<PokemonPageableEntity>()
    private var lastOffset = 0
    private var lastLimit = 0
    private var statusMessage = MutableLiveData<StatusMessage>()

    val getPokemonList: MutableLiveData<List<PokemonPageableEntity>> get() = pokemonList
    val getFilteredPokemonList: MutableLiveData<List<PokemonPageableEntity>> get() = filteredPokemonList
    val getStatusMessage : MutableLiveData<StatusMessage> get() = statusMessage


    fun loadPokemons(offset: Int, limit: Int) {
        if(lastLimit == limit && lastOffset == offset) return

        if(offset == 0) {
            requestPokemonsDatabase(offset, limit)
        }else{
            newPokemonList = mutableListOf()
            for (pokemon in pokemonList.value!!) {
                newPokemonList.add(pokemon)
            }
            requestPokemons(offset, limit)
        }
        lastLimit = limit
        lastOffset = offset
    }

    private fun requestPokemons(offset: Int, limit: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PageableDto> = apiPokeService.getPokemonPageable(offset , limit)
        pokeApi.enqueue(object : Callback<PageableDto> {
            override fun onResponse(
                call: Call<PageableDto>,
                response: Response<PageableDto>,
            ) {
                if (response.body() != null) {
                    savePokemons(response.body()!!)
                    statusMessage.value =
                        StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.SUCCESS)
                    pokemonList.value = newPokemonList
                } else {
                    statusMessage.value =
                        StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.NOT_FOUND)
                }
            }

            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.FAIL)
                requestPokemonsDatabase(offset, limit)
            }
        })
    }

    private fun requestPokemonsDatabase(offset: Int, limit: Int) {
        val pageableDAO = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
        try {
            val resp = pageableDAO.getPagination(offset, limit).toMutableList()
            if (resp.isEmpty()) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.NOT_FOUND)
            } else {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.SUCCESS)
                pokemonList.value = resp
            }
        } catch (e: Exception) {
            statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.FAIL)
        }
    }

    private fun savePokemons(pageablePokemons : PageableDto) {
        val db = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
        for(pokemon in pageablePokemons.results) {
            val pokemonId = Converter.idFromUrl(pokemon.url)
            try {
                val pokemonPageable = PokemonPageableEntity().apply {
                    id = pokemonId
                    name = Converter.beautifyName(pokemon.name)
                    url = pokemon.url
                    image = Converter.urlImageFromId(pokemonId)
                    count = pageablePokemons.count
                }

                val pokemonExists = db.getById(pokemonId)
                if(pokemonExists == null)
                    db.insert(pokemonPageable)
                else
                    db.update(pokemonPageable)

                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.SUCCESS)
                newPokemonList.add(pokemonPageable)

            } catch (e: SQLiteConstraintException){
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.CONSTRAINT, pokemonId)
            } catch (e: Exception) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.FAIL)
            }
        }
    }

    fun searchPokemons(searchText: String){
        var nameOrId = searchText.lowercase().replace("[^a-z\\d]".toRegex(), "-")
        nameOrId = if (nameOrId.toIntOrNull() == null) nameOrId else nameOrId.toInt().toString()
        nameOrId = nameOrId.trim('-')

        if(nameOrId.isEmpty()) {
            if (!filteredPokemonList.value.isNullOrEmpty())
                filteredPokemonList.value = listOf()
            return
        }

        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PokemonDto> = apiPokeService.getPokemonByNameOrId(nameOrId)
        pokeApi.enqueue(object : Callback<PokemonDto> {
            override fun onResponse(
                call: Call<PokemonDto>,
                response: Response<PokemonDto>,
            ) {
                if (response.body() != null) {
                    val pokemonDto = response.body() as PokemonDto

                    val item = PageableItemDto().apply {
                        name = Converter.beautifyName(pokemonDto.name)
                        url = "${Constants.API.BASE_URL_POKEAPI}pokemon/${pokemonDto.id}/"
                    }
                    val pageable = PageableDto().apply {
                        count = 0
                        next = ""
                        previous = null
                        results = listOf(item)
                    }

                    statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.SUCCESS)
                    newPokemonList = mutableListOf()
                    savePokemons(pageable)
                    filteredPokemonList.value = newPokemonList
                } else {
                    statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.NOT_FOUND)
                }
            }

            override fun onFailure(call: Call<PokemonDto>, t: Throwable) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.FAIL)
                searchPokemonsDatabase(searchText)
            }
        })
    }

    private fun searchPokemonsDatabase(searchText: String) {
        if (searchText.isEmpty()) return

        if (searchText.toIntOrNull() != null) {
            val pokemon = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO().getById(searchText.toInt())
            if (pokemon != null) {
                newPokemonList = mutableListOf()
                newPokemonList.add(pokemon)
                filteredPokemonList.value = newPokemonList
            } else {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.NOT_FOUND)
            }
        } else {
            val enclosedSearchText = "%$searchText%"
            val pokemons = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO().getByName(enclosedSearchText)
            if (pokemons.isNotEmpty()) {
                newPokemonList = mutableListOf()
                newPokemonList.addAll(pokemons.filterNotNull())
                filteredPokemonList.value = newPokemonList
            } else {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.NOT_FOUND)
            }
        }
    }

}