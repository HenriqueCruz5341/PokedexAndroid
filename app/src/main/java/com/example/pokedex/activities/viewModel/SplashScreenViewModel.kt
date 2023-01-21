package com.example.pokedex.activities.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.repository.database.model.TypeRelationEntity
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.MyCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreenViewModel(application: Application): AndroidViewModel(application) {

    fun requestPokemons(callback: MyCallback) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PageableDto> = apiPokeService.getPokemonPageable(0 , 30)
        pokeApi.enqueue(object : Callback<PageableDto> {
            override fun onResponse(
                call: Call<PageableDto>,
                response: Response<PageableDto>,
            ) {
                savePokemons(response.body()!!)
                callback.run()
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

    // TODO refactor
    fun saveTypes() {
        val dbTypes = ClientDatabase.getDatabase(getApplication()).TypeDAO()
        val dbTypesRelation = ClientDatabase.getDatabase(getApplication()).TypeRelationDAO()
        var msg = 0

        val listTypeEntity: MutableList<TypeEntity> = mutableListOf()
        Constants.TYPES.listTypes.forEachIndexed { index, s ->
            listTypeEntity.add(TypeEntity().apply {
                id = index+1
                name = s
            })
        }

        if(dbTypes.getAll().isEmpty()) {
            print("a lista é essa ")
            listTypeEntity.forEach{
                try {
                    print("${it.name} ")
                    dbTypes.insert(it)

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
            println("")
        }

        if(dbTypesRelation.getAll().isEmpty()) {
            for (i in 0 until Constants.TYPES.typeJsonArray.length()) {
                try {
                    val typeJson = Constants.TYPES.typeJsonArray.getJSONObject(i)
                    val typeAtackName = typeJson.getString("name").lowercase()
                    val immunesJson = typeJson.getJSONArray("immunes")
                    val weaknessJson = typeJson.getJSONArray("weaknesses")
                    val strengthJson = typeJson.getJSONArray("strengths")
                    val atackPokemon = dbTypes.getByName(typeAtackName)

                    // immunes
                    for(j in 0 until immunesJson.length()) {
                        val typeDefenseName = immunesJson.getString(j).lowercase()
                        val defensePokemon = dbTypes.getByName(typeDefenseName)
                        if(atackPokemon != null && defensePokemon != null) {
                            val typeRelation = TypeRelationEntity().apply {
                                attack_id = atackPokemon.id
                                defense_id = defensePokemon.id
                                multiplaier = 0f
                            }
                            dbTypesRelation.insert(typeRelation)

                        }
                    }

                    // weakness
                    for(j in 0 until weaknessJson.length()) {
                        val typeDefenseName = weaknessJson.getString(j).lowercase()
                        val defensePokemon = dbTypes.getByName(typeDefenseName)
                        if(atackPokemon != null && defensePokemon != null) {
                            val typeRelation = TypeRelationEntity().apply {
                                attack_id = atackPokemon.id
                                defense_id = defensePokemon.id
                                multiplaier = 0.5f
                            }
                            dbTypesRelation.insert(typeRelation)

                        }
                    }

                    // strength
                    for(j in 0 until strengthJson.length()) {
                        val typeDefenseName = strengthJson.getString(j).lowercase()
                        val defensePokemon = dbTypes.getByName(typeDefenseName)
                        if(atackPokemon != null && defensePokemon != null) {
                            val typeRelation = TypeRelationEntity().apply {
                                attack_id = atackPokemon.id
                                defense_id = defensePokemon.id
                                multiplaier = 2f
                            }
                            dbTypesRelation.insert(typeRelation)

                        }
                    }

                } catch (e: SQLiteConstraintException) {
                    msg = Constants.BD_MSGS.CONSTRAINT
                } catch (e: Exception) {
                    msg = Constants.BD_MSGS.FAIL
                } finally {
                    if (msg != 0) {
                        println("Erro ao inserir no banco, code: $msg")
                        msg = 0
                    }
                }
            }
        }
    }
}