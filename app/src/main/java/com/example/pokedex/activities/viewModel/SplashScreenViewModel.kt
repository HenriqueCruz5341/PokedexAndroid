package com.example.pokedex.activities.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.MyCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreenViewModel(application: Application): AndroidViewModel(application) {

    private var pokemonsLoaded: Boolean = false

    fun getPokemonsLoaded(): Boolean {
        return pokemonsLoaded
    }

    fun requestPokemons(callback: MyCallback) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PageableDto> = apiPokeService.getPokemonPageable(0 , 30)
        pokeApi.enqueue(object : Callback<PageableDto> {
            override fun onResponse(
                call: Call<PageableDto>,
                response: Response<PageableDto>,
            ) {
                pokemonsLoaded = true
                savePokemons(response.body()!!)
                callback.run()
            }
            //TODO: Handle error
            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
                pokemonsLoaded = false
            }
        })
    }

    private fun savePokemons(pageablePokemons : PageableDto) {
        val db = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
        var msg = 0
        for(pokemon in pageablePokemons.results) {
            try {
                val pokemonId = pokemon.url.split("/")[6].toInt()
                val pokemonPageable = PokemonPageableEntity().apply {
                    id = pokemonId
                    name = pokemon.name
                    url = pokemon.url
                    image = Constants.API.URL_IMAGES_POKEMON.replace("{{id}}", pokemonId.toString())
                }

                val pokemonExists = db.getById(pokemonId)
                if(pokemonExists == null)
                    db.insert(pokemonPageable)
                else
                    db.update(pokemonPageable)

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
}