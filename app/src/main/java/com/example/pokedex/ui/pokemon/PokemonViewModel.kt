package com.example.pokedex.ui.pokemon

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.api.client.ClientPokeApi
import com.example.pokedex.repository.api.model.pokemon.PokemonDto
import com.example.pokedex.repository.api.service.PokeApiService
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonEntity
import com.example.pokedex.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.concurrent.Executors

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private var pokemon = MutableLiveData<PokemonEntity>()
    private var shinyButton = MutableLiveData(false)
    private var genderButtons = MutableLiveData(Constants.GENDERS.MALE)
    private var imgDefault = MutableLiveData<Bitmap>()
    private var imgFemale = MutableLiveData<Bitmap>()
    private var imgShiny = MutableLiveData<Bitmap>()
    private var imgShinyFemale = MutableLiveData<Bitmap>()

    val getPokemon: MutableLiveData<PokemonEntity> get() = pokemon
    val getShinyButton: MutableLiveData<Boolean> get() = shinyButton
    val getGenderButtons: MutableLiveData<Int> get() = genderButtons
    val getImgDefault: MutableLiveData<Bitmap> get() = imgDefault
    val getImgFemale: MutableLiveData<Bitmap> get() = imgFemale
    val getImgShiny: MutableLiveData<Bitmap> get() = imgShiny
    val getImgShinyFemale: MutableLiveData<Bitmap> get() = imgShinyFemale


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
                savePokemon(response.body()!!)
            }
            //TODO: Handle error
            override fun onFailure(call: Call<PokemonDto>, t: Throwable) {
            }
        })
    }

    fun setShinyButtonToggle() {
        shinyButton.value = !shinyButton.value!!
    }

    fun setGenderButtons(gender: Int) {
        when (gender){
            Constants.GENDERS.MALE -> genderButtons.value = gender
            Constants.GENDERS.FEMALE -> genderButtons.value = gender
            else -> genderButtons.value = Constants.GENDERS.GENDERLESS
        }
    }

    private fun savePokemon(pokemonDto : PokemonDto) {
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
            }

            val pokemonExists = db.getById(pokemonEntity.id)
            if(pokemonExists == null)
                db.insert(pokemonEntity)
            else
                db.update(pokemonEntity)

            pokemon.value = pokemonEntity
            loadImages(pokemonEntity)

        } catch (e: SQLiteConstraintException){
            msg = Constants.BD_MSGS.CONSTRAINT
        } catch (e: Exception) {
            msg = Constants.BD_MSGS.FAIL
        }
    }

    private fun loadImages(pokemonEntity: PokemonEntity){
        loadImageBitmap(pokemonEntity.imgDefault, object : MyCallback {
            override fun run(value: Bitmap) {
                imgDefault.value = value
            }
        })
        loadImageBitmap(pokemonEntity.imgFemale, object : MyCallback {
            override fun run(value: Bitmap) {
                imgFemale.value = value
            }
        })
        loadImageBitmap(pokemonEntity.imgShiny, object : MyCallback {
            override fun run(value: Bitmap) {
                imgShiny.value = value
            }
        })
        loadImageBitmap(pokemonEntity.imgShinyFemale, object : MyCallback {
            override fun run(value: Bitmap) {
                imgShinyFemale.value = value
            }
        })
    }

    private fun loadImageBitmap(url: String, callback: MyCallback) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val stream = URL(url).openStream()
                val image = BitmapFactory.decodeStream(stream)

                handler.post {
                    callback.run(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private interface MyCallback {
        fun run(value: Bitmap)
    }
}