package com.example.pokedex.ui.home

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var listMsg = MutableLiveData<Int>()
    private var pokemonList = MutableLiveData<List<PokemonPageableEntity>>()
    private var filteredPokemonList = MutableLiveData<List<PokemonPageableEntity>>()
    private var newPokemonList = mutableListOf<PokemonPageableEntity>()
    private var lastOffset = 0
    private var lastLimit = 0

    val getListMsg: LiveData<Int> get() = listMsg
    val getPokemonList: MutableLiveData<List<PokemonPageableEntity>> get() = pokemonList
    val getFilteredPokemonList: MutableLiveData<List<PokemonPageableEntity>> get() = filteredPokemonList

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
                savePokemons(response.body()!!)
                pokemonList.value = newPokemonList
            }

            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
                requestPokemonsDatabase(offset, limit)
            }
        })
    }

    private fun requestPokemonsDatabase(offset: Int, limit: Int) {
        val pageableDAO = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
        try {
            val resp = pageableDAO.getPagination(offset, limit).toMutableList()
            if (resp.isEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                pokemonList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    private fun savePokemons(pageablePokemons : PageableDto) {
        val db = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
        var msg = 0
        for(pokemon in pageablePokemons.results) {
            try {
                val pokemonId = Converter.idFromUrl(pokemon.url)
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

                newPokemonList.add(pokemonPageable)

            } catch (e: SQLiteConstraintException){
                msg = Constants.BD_MSGS.CONSTRAINT
            } catch (e: Exception) {
                msg = Constants.BD_MSGS.FAIL
            } finally {
                if(msg != 0) {
                    println("Erro ao inserir no banco, code: $msg")
                    msg = 0
                }
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

                    newPokemonList = mutableListOf()
                    savePokemons(pageable)
                    filteredPokemonList.value = newPokemonList
                }
            }

            override fun onFailure(call: Call<PokemonDto>, t: Throwable) {
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
            }
        } else {
            val enclosedSearchText = "%$searchText%"
            val pokemons = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO().getByName(enclosedSearchText)
            newPokemonList = mutableListOf()
            newPokemonList.addAll(pokemons.filterNotNull())
            filteredPokemonList.value = newPokemonList
        }
    }

}