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
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.repository.database.model.PokemonEntity
import com.example.pokedex.repository.database.model.VarietyEntity
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Converter
import com.example.pokedex.utils.ImageURL
import com.example.pokedex.utils.StatusMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * PokemonViewModel is the ViewModel of Pokemon Fragment
 *
 * It os responsible for the communication between the repository and the fragment, doing the requests
 * and saving the data in the database. It also has the logic to get the data only from the database
 * if no internet connection and send it to the fragment.
 *
 * @param application is the application context
 * @property pokemon is the pokemon data
 * @property shinyButton is the state of shiny button
 * @property genderButtons is the state of gender buttons
 * @property imgDefault is the default image of the pokemon
 * @property imgFemale is the female image of the pokemon
 * @property imgShiny is the shiny image of the pokemon
 * @property imgShinyFemale is the female shine image of the pokemon
 * @property customEvolutionList is the evolution list of the pokemon
 * @property varietyList is the variety list of the pokemon
 * @property typeRelationList is the type relation list of the pokemon
 * @property statusMessage is the status message for some errors
 *
 */
class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private var pokemon = MutableLiveData<PokemonEntity>()
    private var shinyButton = MutableLiveData(false)
    private var genderButtons = MutableLiveData(Constants.GENDERS.MALE)
    private var imgDefault = MutableLiveData<Bitmap>()
    private var imgFemale = MutableLiveData<Bitmap>()
    private var imgShiny = MutableLiveData<Bitmap>()
    private var imgShinyFemale = MutableLiveData<Bitmap>()
    private var customEvolutionList = MutableLiveData<List<EvolutionEntity>>()
    private var varietyList = MutableLiveData<List<VarietyEntity>>()
    private var typeRelationList = MutableLiveData<List<TypeMultiplierDTO>>()
    private var statusMessage = MutableLiveData<StatusMessage>()

    private var pokemonDto: PokemonDto = PokemonDto()
    private var pokemonSpecieDto: PokemonSpecieDto = PokemonSpecieDto()
    private var evolutionChainDto: EvolutionChainDto = EvolutionChainDto()

    val getPokemon: MutableLiveData<PokemonEntity> get() = pokemon
    val getShinyButton: MutableLiveData<Boolean> get() = shinyButton
    val getGenderButtons: MutableLiveData<Int> get() = genderButtons
    val getImgDefault: MutableLiveData<Bitmap> get() = imgDefault
    val getImgFemale: MutableLiveData<Bitmap> get() = imgFemale
    val getImgShiny: MutableLiveData<Bitmap> get() = imgShiny
    val getImgShinyFemale: MutableLiveData<Bitmap> get() = imgShinyFemale
    val getCustomEvolutionList: MutableLiveData<List<EvolutionEntity>> get() = customEvolutionList
    val getVarietyList: MutableLiveData<List<VarietyEntity>> get() = varietyList
    val getTypeRelationList: MutableLiveData<List<TypeMultiplierDTO>> get() = typeRelationList
    val getStatusMessage : MutableLiveData<StatusMessage> get() = statusMessage

    /**
     * This method is responsible for loading the pokemon data from the API or the database. If the
     * request to API fails, it will try to get the data from the database. If the request to the
     * database fails, it will send a status message to the fragment. If the request to the API
     * succeeds, call the method to request the pokemon specie, to get more information about the
     * pokemon.
     *
     * @param pokemonId is the id of the pokemon
     */
    fun loadPokemon(pokemonId: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PokemonDto> = apiPokeService.getPokemonById(pokemonId)
        pokeApi.enqueue(object : Callback<PokemonDto> {
            override fun onResponse(
                call: Call<PokemonDto>,
                response: Response<PokemonDto>,
            ) {
                if(response.body() != null) {
                    pokemonDto = response.body() as PokemonDto
                    val specieId = Converter.idFromUrl(pokemonDto.species.url)
                    requestPokemonSpecie(specieId)
                } else {
                    statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.NOT_FOUND)
                }
            }

            override fun onFailure(call: Call<PokemonDto>, t: Throwable) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.FAIL)
                requestPokemonDatabase(pokemonId)
            }
        })
    }

    /**
     * This method is responsible for loading the pokemon data from the database. In this case,
     * all the pokemon information is in the database, so it will not be necessary to request the
     * API, only access the Pokemon, Varieties and EvolutionChain tables. If the request to the
     * database fails, it will send a status message to the fragment. If the request to the database
     * succeeds, it will send the data to the fragment.
     *
     * @param pokemonId is the id of the pokemon
     */
    private fun requestPokemonDatabase(pokemonId: Int) {
        val pokemonDAO = ClientDatabase.getDatabase(getApplication()).PokemonDAO()
        val varietyDAO = ClientDatabase.getDatabase(getApplication()).VarietyDAO()
        val evolutionDAO = ClientDatabase.getDatabase(getApplication()).EvolutionDAO()

        val pokemonEntity = pokemonDAO.getById(pokemonId)
        if (pokemonEntity != null) {
            pokemon.value = pokemonEntity as PokemonEntity
            varietyList.value = varietyDAO.getByPokemonId(pokemonId).filterNotNull()
            val evolutions = evolutionDAO.getChainByPokemonId(pokemonId).filterNotNull()
            loadCustomEvolutionList(evolutions.toMutableList())
        } else {
            statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.NOT_FOUND)
        }
    }

    /**
     * This method is responsible for loading the pokemon specie data from the API or the database.
     * If the request to API fails, it will try to get the data from the database. If the request to
     * the database fails, it will send a status message to the fragment. If the request to the API
     * succeeds, call the method to request the evolution chain, to get more information about the
     * pokemon.
     *
     * @param id is the id of the pokemon specie
     */
    private fun requestPokemonSpecie(id: Int) {
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<PokemonSpecieDto> = apiPokeService.getPokemonSpecieById(id)
        pokeApi.enqueue(object : Callback<PokemonSpecieDto> {
            override fun onResponse(
                call: Call<PokemonSpecieDto>,
                response: Response<PokemonSpecieDto>,
            ) {
                if(response.body() != null) {
                    pokemonSpecieDto = response.body() as PokemonSpecieDto
                    val evolutionId = Converter.idFromUrl(pokemonSpecieDto.evolutionChain.url)
                    requestEvolutionChain(evolutionId)
                } else {
                    statusMessage.value = StatusMessage(Constants.RES_MSGS.SPECIE, Constants.API_MSGS.NOT_FOUND)
                }
            }
            override fun onFailure(call: Call<PokemonSpecieDto>, t: Throwable) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.SPECIE, Constants.API_MSGS.FAIL)
                requestPokemonDatabase(Converter.idFromUrl(pokemonDto.species.url))
            }
        })
    }

    /**
     * This method is responsible for loading the evolution chain data from the API or the database.
     * If the request to API fails, it will try to get the data from the database. If the request to
     * the database fails, it will send a status message to the fragment. If the request to the API
     * succeeds, call the method to save the pokemon in the database.
     *
     * @param id is the id of the evolution chain
     */
    private fun requestEvolutionChain(id: Int){
        val apiPokeService = ClientPokeApi.createService(PokeApiService::class.java)
        val pokeApi: Call<EvolutionChainDto> = apiPokeService.getEvolutionChainById(id)
        pokeApi.enqueue(object : Callback<EvolutionChainDto> {
            override fun onResponse(
                call: Call<EvolutionChainDto>,
                response: Response<EvolutionChainDto>
            ) {
                if(response.body() != null) {
                    evolutionChainDto = response.body() as EvolutionChainDto
                    savePokemon()
                } else {
                    statusMessage.value = StatusMessage(Constants.RES_MSGS.EVOLUTION, Constants.API_MSGS.NOT_FOUND)
                }
            }
            override fun onFailure(call: Call<EvolutionChainDto>, t: Throwable) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.EVOLUTION, Constants.API_MSGS.FAIL)
                requestPokemonDatabase(Converter.idFromUrl(pokemonDto.species.url))
            }

        })
    }

    /**
     * This method is responsible for saving the pokemon data from API in the database. If the
     * request to the database fails, it will send a status message to the fragment. If the request
     * to the database succeeds, it will send the data to the fragment. Also it will call the method
     * to load pokemon images, save the pokemon varieties and evolution chain in the database.
     */
    private fun savePokemon() {
        val db = ClientDatabase.getDatabase(getApplication()).PokemonDAO()

        try {
            val pokemonEntity = PokemonEntity().apply {
                id = pokemonDto.id
                name = Converter.beautifyName(pokemonDto.name)
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
            statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.API_MSGS.SUCCESS)
            loadImages(pokemonEntity)

            saveVarieties()
            saveEvolutions()

        } catch (e: SQLiteConstraintException){
            statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.CONSTRAINT)
        } catch (e: Exception) {
            statusMessage.value = StatusMessage(Constants.RES_MSGS.POKEMON, Constants.DB_MSGS.FAIL)
        }
    }

    /**
     * This method is responsible for saving the pokemon varieties data from API in the database. If
     * the request to the database fails, it will send a status message to the fragment. If the
     * request to the database succeeds, it will send the data to the fragment.
     */
    private fun saveVarieties(){
        val varietyDAO = ClientDatabase.getDatabase(getApplication()).VarietyDAO()
        val list = mutableListOf<VarietyEntity>()

        val varieties = pokemonSpecieDto.varieties
        varieties.forEach {
            val varietyId = Converter.idFromUrl(it.pokemon.url)
            try {
                val variety = VarietyEntity().apply {
                    id = varietyId
                    pokemonId = pokemonSpecieDto.id
                    pokemonName = Converter.beautifyName(it.pokemon.name)
                    isDefault = it.isDefault
                }

                val varietyExists = varietyDAO.getById(variety.id)
                if(varietyExists == null)
                    varietyDAO.insert(variety)
                else
                    varietyDAO.update(variety)

                list.add(variety)
            } catch (e: SQLiteConstraintException){
                statusMessage.value = StatusMessage(Constants.RES_MSGS.SPECIE, Constants.DB_MSGS.CONSTRAINT, varietyId)
            } catch (e: Exception) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.SPECIE, Constants.DB_MSGS.FAIL)
            }
        }

        varietyList.value = list
        statusMessage.value = StatusMessage(Constants.RES_MSGS.SPECIE, Constants.API_MSGS.SUCCESS)
    }

    /**
     * This method is responsible for saving the pokemon evolutions data from API in the database. If
     * the request to the database fails, it will send a status message to the fragment. If the
     * request to the database succeeds, it will send the data to the fragment. A particularity of
     * this method is that it will call the method to linearize the evolution chain, because the
     * evolution chain is a tree, and the database only accepts linear data.
     */
    private fun saveEvolutions() {
        val evolutionDAO = ClientDatabase.getDatabase(getApplication()).EvolutionDAO()
        val firstEvolutionId = Converter.idFromUrl(evolutionChainDto.chain.species.url)
        val firstEvolution = EvolutionEntity().apply {
            id = firstEvolutionId
            chain = evolutionChainDto.id
            order = 0
            pokemonName = Converter.beautifyName(evolutionChainDto.chain.species.name)
            pokemonImage = Converter.urlImageFromId(firstEvolutionId)
        }
        val evolutions = mutableListOf(firstEvolution)
        evolutions.addAll(linearizeEvolutionChain(evolutionChainDto.chain.evolvesTo, 1))

        evolutions.forEach {
            try {
                val evolutionExists = evolutionDAO.getById(it.id)
                if (evolutionExists == null)
                    evolutionDAO.insert(it)
                else
                    evolutionDAO.update(it)

            } catch (e: SQLiteConstraintException) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.EVOLUTION, Constants.DB_MSGS.CONSTRAINT, it.id)
            } catch (e: Exception) {
                statusMessage.value = StatusMessage(Constants.RES_MSGS.EVOLUTION, Constants.DB_MSGS.FAIL)
            }
        }

        statusMessage.value = StatusMessage(Constants.RES_MSGS.EVOLUTION, Constants.API_MSGS.SUCCESS)
        loadCustomEvolutionList(evolutions)
    }

    /**
     * This method is responsible for loading the custom evolution chain data a normal evolution
     * chain. The difference between the two is that the normal evolution chain has only evolutions
     * information, and the custom evolution chain has information about the evolutions and every
     * time a pokemon evolves, it will have a fake evolution to separate the evolutions. This fake
     * evolution will be used to show an arrow instead a pokemon, this logic is implemented in the
     * ListEvolutionAdapter.
     *
     * @param evolutions The list of evolutions from the normal evolution chain.
     * @see com.example.pokedex.ui.recycleView.evolution.ListEvolutionAdapter
     *
     */
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

    /**
     * This method is responsible for linearizing the evolution chain. The evolution chain is a tree,
     * and the database only accepts linear data. This method will receive a list of evolutions, and
     * will return a list of evolutions, but the list will be linearized. To do this, it will call
     * itself recursively, until it reaches the end of the tree.
     *
     * @param evolvesToDto The list of evolutions to be linearized.
     * @param order The order of the evolution.
     * @return A list of evolutions, but linearized.
     */
    private fun linearizeEvolutionChain(evolvesToDto: List<EvolvesToDto>, order: Int): List<EvolutionEntity> {
        val list = mutableListOf<EvolutionEntity>()

        if(evolvesToDto.isEmpty()) {
            return list
        } else {
            evolvesToDto.forEach {
                val pokemonId = Converter.idFromUrl(it.species.url)
                val evolution = EvolutionEntity().apply {
                    id = pokemonId
                    chain = evolutionChainDto.id
                    this.order = order
                    pokemonName = Converter.beautifyName(it.species.name)
                    pokemonImage = Converter.urlImageFromId(pokemonId)
                }
                list.add(evolution)
                list.addAll(linearizeEvolutionChain(it.evolvesTo, order + 1))
            }
        }

        return list
    }

    /**
     * This method is responsible for load all pokemon images. This method will call the ImageURL
     * class, which is responsible for loading the images from an url to a Bitmap. This method also
     * checks if pokemon has male and female genders, because if it doesn't, it will not load the
     * images, for optimization purposes.
     *
     * @param pokemonEntity The pokemon entity with the images urls.
     */
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

    /**
     * This method is responsible for configure the type relation list. This method will get the
     * type relation list from the database, and will configure it. The logic
     * is simple, it will group the list by the attack type, and will multiply all the damage
     * multipliers of the same attack type. This will result in a list of TypeMultiplierDTO, which
     * will be used in the UI.
     *
     * @param typeOne The first type of the pokemon.
     */
    fun configureTypeRelationList(typeOne: String, typeTwo: String?) {
        val typeRelationDAO = ClientDatabase.getDatabase(getApplication()).TypeRelationDAO()

        val typesRelationEntity = typeRelationDAO.getByTypesName(typeOne, typeTwo ?: "")

        this.typeRelationList.value = typesRelationEntity.groupBy { it.attack }.map {
            TypeMultiplierDTO(
                it.key,
                it.value.map { it2 -> it2.damageMultiplier }.reduce { acc, i -> acc * i })
        }.sortedBy { it.multiplier }
    }

    /**
     * This method is responsible to toggle the shiny button. If the shiny button is true, it will
     * set it to false, and vice versa.
     */
    fun setShinyButtonToggle() {
        shinyButton.value = !shinyButton.value!!
    }

    /**
     * This method is responsible to set a value to genderButtons, that controls the state.
     * Basically if the gender passed as parameter, is a valid gender, it will set the genderButtons
     * otherwise it will set the genderButtons to GENDERLESS.
     *
     * @param gender is the gender to be set.
     */
    fun setGenderButtons(gender: Int) {
        when (gender){
            Constants.GENDERS.MALE -> genderButtons.value = gender
            Constants.GENDERS.FEMALE -> genderButtons.value = gender
            else -> genderButtons.value = Constants.GENDERS.GENDERLESS
        }
    }
}