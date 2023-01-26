package com.example.pokedex.ui.home

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
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
    private var pokemonList = MutableLiveData<MutableList<PokemonPageableEntity>>()
    private var newPokemonList = mutableListOf<PokemonPageableEntity>()

    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getPokemonList(): LiveData<MutableList<PokemonPageableEntity>> {
        return pokemonList
    }

    fun loadPokemons(offset: Int, limit: Int) {
//        Log.d("HOMEVIEWMODEL","loadPokemons")
//        Log.d("HOMEVIEWMODEL", "offset: $offset | limit: $limit")
        if(offset == 0) {
            val db = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
            try {
                val resp = db.getPagination(offset, limit).toMutableList()
                if (resp.isEmpty()) {
                    listMsg.value = Constants.BD_MSGS.NOT_FOUND
                } else {
                    listMsg.value = Constants.BD_MSGS.SUCCESS
                    pokemonList.value = resp
                }
            } catch (e: Exception) {
                listMsg.value = Constants.BD_MSGS.FAIL
            }
        }else{
            //TODO: Refactor this code
            newPokemonList = mutableListOf()
            for (pokemon in pokemonList.value!!) {
                newPokemonList.add(pokemon)
            }
            requestPokemons(offset, limit)
        }
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
            }
            //TODO: Handle error
            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
            }
        })
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

        pokemonList.value = newPokemonList
    }

}