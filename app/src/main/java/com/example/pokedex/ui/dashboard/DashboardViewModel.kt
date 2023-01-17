package com.example.pokedex.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.utils.Constants

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private var listMsg = MutableLiveData<Int>()
    private var typeList = MutableLiveData<List<TypeEntity>>()

    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getTypeList(): LiveData<List<TypeEntity>> {
        return typeList
    }

    fun getAllTypes() {
        val db = ClientDatabase.getDatabase(getApplication()).TypeDAO()
        try {
            val resp = db.getAll()
            if (resp.isEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                typeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }
}