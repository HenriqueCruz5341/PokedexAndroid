package com.example.pokedex.activities.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.PageableDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.repository.database.model.TypeRelationEntity
import com.example.pokedex.utils.Constants.*
import com.example.pokedex.utils.Converter
import com.example.pokedex.utils.StatusMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreenViewModel(application: Application): AndroidViewModel(application) {

    private var apiMsg = MutableLiveData<StatusMessage>()
    private var dbMsg = MutableLiveData<StatusMessage>()

    val getApiMsg : MutableLiveData<StatusMessage> get() = apiMsg
    val getDbMsg : MutableLiveData<StatusMessage> get() = dbMsg

    fun requestPokemons() {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PageableDto> = apiPokeService.getPokemonPageable(0, 30)

        pokeApi.enqueue(object : Callback<PageableDto> {
            override fun onResponse(
                call: Call<PageableDto>,
                response: Response<PageableDto>,
            ) {
                if(response.body() != null) {
                    savePokemons(response.body() as PageableDto)
                    apiMsg.value = StatusMessage(RES_MSGS.POKEMON, API_MSGS.SUCCESS)
                }
                else apiMsg.value = StatusMessage(RES_MSGS.POKEMON, API_MSGS.NOT_FOUND)
            }

            override fun onFailure(call: Call<PageableDto>, t: Throwable) {
                apiMsg.value = StatusMessage(RES_MSGS.POKEMON, API_MSGS.FAIL)
            }
        })
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

                dbMsg.value = StatusMessage(RES_MSGS.POKEMON, DB_MSGS.SUCCESS)

            } catch (e: SQLiteConstraintException){
                dbMsg.value = StatusMessage(RES_MSGS.POKEMON, DB_MSGS.CONSTRAINT, pokemonId)
            } catch (e: Exception) {
                dbMsg.value = StatusMessage(RES_MSGS.POKEMON, DB_MSGS.FAIL, pokemonId)
            }
        }
    }

    fun saveTypes() {
        val dbTypes = ClientDatabase.getDatabase(getApplication()).TypeDAO()
        val dbTypesRelation = ClientDatabase.getDatabase(getApplication()).TypeRelationDAO()

        val listTypeEntity: MutableList<TypeEntity> = mutableListOf()
        TYPES.listTypes.forEachIndexed { index, s ->
            listTypeEntity.add(TypeEntity().apply {
                id = index+1
                name = s
            })
        }

        if(dbTypes.getAll().isEmpty()) {
            listTypeEntity.forEach{
                try {
                    dbTypes.insert(it)
                    dbMsg.value = StatusMessage(RES_MSGS.TYPE, DB_MSGS.SUCCESS)

                } catch (e: SQLiteConstraintException){
                    dbMsg.value = StatusMessage(RES_MSGS.TYPE, DB_MSGS.CONSTRAINT)
                } catch (e: Exception) {
                    dbMsg.value = StatusMessage(RES_MSGS.TYPE, DB_MSGS.FAIL)
                }
            }
        }

        if(dbTypesRelation.getAll().isEmpty()) {
            for (i in 0 until TYPES.typeJsonArray.length()) {
                try {
                    val typeJson = TYPES.typeJsonArray.getJSONObject(i)
                    val typeAttackName = typeJson.getString("name").lowercase()
                    val immuneJson = typeJson.getJSONArray("immunes")
                    val weaknessJson = typeJson.getJSONArray("weaknesses")
                    val strengthJson = typeJson.getJSONArray("strengths")
                    val attackPokemon = dbTypes.getByName(typeAttackName)

                    // immune
                    for(j in 0 until immuneJson.length()) {
                        val typeDefenseName = immuneJson.getString(j).lowercase()
                        val defensePokemon = dbTypes.getByName(typeDefenseName)
                        if(attackPokemon != null && defensePokemon != null) {
                            val typeRelation = TypeRelationEntity().apply {
                                attack_id = attackPokemon.id
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
                        if(attackPokemon != null && defensePokemon != null) {
                            val typeRelation = TypeRelationEntity().apply {
                                attack_id = attackPokemon.id
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
                        if(attackPokemon != null && defensePokemon != null) {
                            val typeRelation = TypeRelationEntity().apply {
                                attack_id = attackPokemon.id
                                defense_id = defensePokemon.id
                                multiplaier = 2f
                            }
                            dbTypesRelation.insert(typeRelation)
                            dbMsg.value = StatusMessage(RES_MSGS.TYPE_REL, DB_MSGS.SUCCESS)
                        }
                    }

                } catch (e: SQLiteConstraintException) {
                    dbMsg.value = StatusMessage(RES_MSGS.TYPE_REL, DB_MSGS.CONSTRAINT)
                } catch (e: Exception) {
                    dbMsg.value = StatusMessage(RES_MSGS.TYPE_REL, DB_MSGS.FAIL)
                }
            }
        }
    }
}