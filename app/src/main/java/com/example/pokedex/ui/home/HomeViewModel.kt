package com.example.pokedex.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.utils.Constants

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var listMsg = MutableLiveData<Int>()
    private var pokemonList = MutableLiveData<List<PokemonPageableEntity>>()

    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getPokemonList(): LiveData<List<PokemonPageableEntity>> {
        return pokemonList
    }

    fun getAllPokemons() {
        val db = ClientDatabase.getDatabase(getApplication()).PokemonPageableDAO()
        try {
            val resp = db.getAll()
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
}