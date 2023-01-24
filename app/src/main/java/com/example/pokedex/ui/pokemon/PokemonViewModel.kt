package com.example.pokedex.ui.pokemon

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.evolution.EvolutionChainDto
import com.example.pokedex.repository.api.model.evolution.EvolvesToDto
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import com.example.pokedex.repository.api.model.pokemonSpecie.PokemonSpecieDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.repository.database.model.PokemonEntity
import com.example.pokedex.repository.database.model.VarietyEntity
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.ImageURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private var pokemon = MutableLiveData<PokemonEntity>()
    private var shinyButton = MutableLiveData(false)
    private var genderButtons = MutableLiveData(Constants.GENDERS.MALE)
    private var imgDefault = MutableLiveData<Bitmap>()
    private var imgFemale = MutableLiveData<Bitmap>()
    private var imgShiny = MutableLiveData<Bitmap>()
    private var imgShinyFemale = MutableLiveData<Bitmap>()
    private var customEvolutionList = MutableLiveData<List<EvolutionEntity>>()

    private var _pokemonDto: PokemonDto? = null
    private var _pokemonSpecieDto: PokemonSpecieDto? = null
    private var _evolutionChainDto: EvolutionChainDto? = null
    private val pokemonDto get() = _pokemonDto!!
    private val pokemonSpecieDto get() = _pokemonSpecieDto!!
    private val evolutionChainDto get() = _evolutionChainDto!!

    val getPokemon: MutableLiveData<PokemonEntity> get() = pokemon
    val getShinyButton: MutableLiveData<Boolean> get() = shinyButton
    val getGenderButtons: MutableLiveData<Int> get() = genderButtons
    val getImgDefault: MutableLiveData<Bitmap> get() = imgDefault
    val getImgFemale: MutableLiveData<Bitmap> get() = imgFemale
    val getImgShiny: MutableLiveData<Bitmap> get() = imgShiny
    val getImgShinyFemale: MutableLiveData<Bitmap> get() = imgShinyFemale
    val getCustomEvolutionList: MutableLiveData<List<EvolutionEntity>> get() = customEvolutionList

    fun loadPokemon(id: Int) {
        requestPokemon(id)
    }


    private fun requestPokemon(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PokemonDto> = apiPokeService.getPokemonById(id)
        pokeApi.enqueue(object : Callback<PokemonDto> {
            override fun onResponse(
                call: Call<PokemonDto>,
                response: Response<PokemonDto>,
            ) {
                _pokemonDto = response.body()
                val specieId = _pokemonDto?.species?.url?.split("/")?.get(6)?.toInt()
                if (specieId != null) {
                    requestPokemonSpecie(specieId)
                }
            }
            //TODO: Handle error
            override fun onFailure(call: Call<PokemonDto>, t: Throwable) {
            }
        })
    }

    private fun requestPokemonSpecie(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PokemonSpecieDto> = apiPokeService.getPokemonSpecieById(id)
        pokeApi.enqueue(object : Callback<PokemonSpecieDto> {
            override fun onResponse(
                call: Call<PokemonSpecieDto>,
                response: Response<PokemonSpecieDto>,
            ) {
                _pokemonSpecieDto = response.body()
                val evolutionId = _pokemonSpecieDto?.evolutionChain?.url?.split("/")?.get(6)?.toInt()
                if (evolutionId != null) {
                    requestEvolutionChain(evolutionId)
                }
            }
            //TODO: Handle error
            override fun onFailure(call: Call<PokemonSpecieDto>, t: Throwable) {
            }
        })
    }

    private fun requestEvolutionChain(id: Int){
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<EvolutionChainDto> = apiPokeService.getEvolutionChainById(id)
        pokeApi.enqueue(object : Callback<EvolutionChainDto> {
            override fun onResponse(
                call: Call<EvolutionChainDto>,
                response: Response<EvolutionChainDto>
            ) {
                _evolutionChainDto = response.body()
                if (_evolutionChainDto != null) {
                    savePokemon()
                }
            }
            override fun onFailure(call: Call<EvolutionChainDto>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setShinyButtonToggle() {
        shinyButton.value = !shinyButton.value!!
    }

    fun setGenderButtons(gender: Int) {
        // gender_rate, = -1 genderless, = 0 100% male,  = 8 100% female
        when (gender){
            Constants.GENDERS.MALE -> genderButtons.value = gender
            Constants.GENDERS.FEMALE -> genderButtons.value = gender
            else -> genderButtons.value = Constants.GENDERS.GENDERLESS
        }
    }

    private fun savePokemon() {
        val db = ClientDatabase.getDatabase(getApplication()).PokemonDAO()
        var msg = 0

        try {
            val pokemonEntity = PokemonEntity().apply {
                id = pokemonDto.id
                name = pokemonDto.name
                imgDefault = pokemonDto.sprites.default
                imgFemale = pokemonDto.sprites.female ?: pokemonDto.sprites.default
                imgShiny = pokemonDto.sprites.shiny ?: pokemonDto.sprites.default
                imgShinyFemale = pokemonDto.sprites.shinyFemale ?: pokemonDto.sprites.shiny ?: pokemonDto.sprites.default
                speciesUrl = pokemonDto.species.url
                typeOne = pokemonDto.types[0].name.value
                typeTwo = if(pokemonDto.types.size > 1) pokemonDto.types[1].name.value else null
                statHp = pokemonDto.stats[0].baseStat
                statAttack = pokemonDto.stats[1].baseStat
                statDefense = pokemonDto.stats[2].baseStat
                statSpAttack = pokemonDto.stats[3].baseStat
                statSpDefense = pokemonDto.stats[4].baseStat
                statSpeed = pokemonDto.stats[5].baseStat
                genderRate = pokemonSpecieDto.genderRate
            }

            val pokemonExists = db.getById(pokemonEntity.id)
            if(pokemonExists == null)
                db.insert(pokemonEntity)
            else
                db.update(pokemonEntity)

            pokemon.value = pokemonEntity
            loadImages(pokemonEntity)

            saveVarieties()
            saveEvolutions()

        } catch (e: SQLiteConstraintException){
            msg = Constants.BD_MSGS.CONSTRAINT
        } catch (e: Exception) {
            msg = Constants.BD_MSGS.FAIL
        }
    }

    private fun saveVarieties(){
        val varietyDAO = ClientDatabase.getDatabase(getApplication()).VarietyDAO()
        var msg = 0

        try {
            val varieties = pokemonSpecieDto.varieties
            varieties.forEach {
                val varietyId = it.pokemon.url.split("/")[6].toInt()
                val variety = VarietyEntity().apply {
                    id = varietyId
                    pokemonId = pokemonSpecieDto.id
                    pokemonName = it.pokemon.name
                    isDefault = it.isDefault
                }

                val varietyExists = varietyDAO.getById(variety.id)
                if(varietyExists == null)
                    varietyDAO.insert(variety)
                else
                    varietyDAO.update(variety)
            }
        } catch (e: SQLiteConstraintException){
            msg = Constants.BD_MSGS.CONSTRAINT
        } catch (e: Exception) {
            msg = Constants.BD_MSGS.FAIL
        }
    }

    private fun saveEvolutions() {
        val evolutionDAO = ClientDatabase.getDatabase(getApplication()).EvolutionDAO()
        var msg = 0

        try {
            val firstEvolutionId = evolutionChainDto.chain.species.url.split("/")[6].toInt()
            val firstEvolution = EvolutionEntity().apply {
                id = firstEvolutionId
                chain = evolutionChainDto.id
                order = 0
                pokemonName = evolutionChainDto.chain.species.name
                pokemonImage = Constants.API.URL_IMAGES_POKEMON.replace("{{id}}", firstEvolutionId.toString())
            }
            val evolutions = mutableListOf(firstEvolution)
            evolutions.addAll(linearizeEvolutionChain(evolutionChainDto.chain.evolvesTo, 1))

            evolutions.forEach {
                val evolutionExists = evolutionDAO.getById(it.id)
                if(evolutionExists == null)
                    evolutionDAO.insert(it)
                else
                    evolutionDAO.update(it)
            }

            loadCustomEvolutionList(evolutions)

        } catch (e: SQLiteConstraintException){
            msg = Constants.BD_MSGS.CONSTRAINT
        } catch (e: Exception) {
            msg = Constants.BD_MSGS.FAIL
        }
    }

    private fun loadCustomEvolutionList(evolutions: MutableList<EvolutionEntity>) {
        val list = mutableListOf<EvolutionEntity>()
        var prevOrder = 0
        evolutions.sortBy { it.order }
        evolutions.forEach {
            if(it.order == prevOrder) {
                list.add(it)
            } else {
                list.add(EvolutionEntity().apply {
                    id = -1
                    chain = -1
                    order = it.order
                    pokemonName = ""
                    pokemonImage = ""
                })
                list.add(it)
            }
            prevOrder = it.order
        }
        customEvolutionList.value = list
    }

    private fun linearizeEvolutionChain(evolvesToDto: List<EvolvesToDto>, order: Int): List<EvolutionEntity> {
        val list = mutableListOf<EvolutionEntity>()

        if(evolvesToDto.isEmpty()) {
            return list
        } else {
            evolvesToDto.forEach {
                val pokemonId = it.species.url.split("/")[6].toInt()
                val evolution = EvolutionEntity().apply {
                    id = pokemonId
                    chain = evolutionChainDto.id
                    this.order = order
                    pokemonName = it.species.name
                    pokemonImage = Constants.API.URL_IMAGES_POKEMON.replace("{{id}}", pokemonId.toString())
                }
                list.add(evolution)
                list.addAll(linearizeEvolutionChain(it.evolvesTo, order + 1))
            }
        }

        return list
    }

    private fun loadImages(pokemonEntity: PokemonEntity){
        if (pokemonEntity.genderRate != 8) {
            ImageURL.loadImageBitmap(pokemonEntity.imgDefault, object : ImageURL.OnImageLoaded {
                override fun run(value: Bitmap) {
                    imgDefault.value = value
                }
            })
            ImageURL.loadImageBitmap(pokemonEntity.imgShiny, object : ImageURL.OnImageLoaded {
                override fun run(value: Bitmap) {
                    imgShiny.value = value
                }
            })
        }
        if (pokemonEntity.genderRate != 0 && pokemonEntity.genderRate != -1) {
            ImageURL.loadImageBitmap(pokemonEntity.imgFemale, object : ImageURL.OnImageLoaded {
                override fun run(value: Bitmap) {
                    imgFemale.value = value
                }
            })
            ImageURL.loadImageBitmap(pokemonEntity.imgShinyFemale, object : ImageURL.OnImageLoaded {
                override fun run(value: Bitmap) {
                    imgShinyFemale.value = value
                }
            })
        }
    }
}