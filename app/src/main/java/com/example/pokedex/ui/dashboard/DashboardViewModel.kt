package com.example.pokedex.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.utils.Constants

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private var listMsg = MutableLiveData<Int>()
    private var typeList = MutableLiveData<List<TypeEntity>>()
    private var typeDefenseList = MutableLiveData<List<TypeMultiplierDTO>>()
    private var typeAttackList = MutableLiveData<List<TypeMultiplierDTO>>()

    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getTypeList(): LiveData<List<TypeEntity>> {
        return typeList
    }

    fun getTypeEffectiveness(): LiveData<List<TypeMultiplierDTO>> {
        return typeAttackList
    }

    fun getTypeWeakness(): LiveData<List<TypeMultiplierDTO>> {
        return typeDefenseList
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

    fun getAllEffectiveness(typeId: Int) {
        val dbTypes = ClientDatabase.getDatabase(getApplication()).TypeDAO()
        val dbRelation = ClientDatabase.getDatabase(getApplication()).TypeRelationDAO()
        try {
            val attack = dbRelation.getAttack(typeId)
            val resp: MutableList<TypeMultiplierDTO> = mutableListOf()
            if (attack.isEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                attack.forEach {
                    val type = dbTypes.getById(it.defense_id)
                    if (type != null)
                        resp.add(TypeMultiplierDTO(type.name, it.multiplaier))
                }
                typeAttackList.value = resp.toList()
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    fun getAllWeakness(typeId: Int) {
        val dbTypes = ClientDatabase.getDatabase(getApplication()).TypeDAO()
        val dbRelation = ClientDatabase.getDatabase(getApplication()).TypeRelationDAO()
        try {
            val defense = dbRelation.getDefense(typeId)
            val resp: MutableList<TypeMultiplierDTO> = mutableListOf()
            if (defense.isEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                defense.forEach {
                    val type = dbTypes.getById(it.attack_id)
                    if (type != null)
                        resp.add(TypeMultiplierDTO(type.name, it.multiplaier))
                }
                typeDefenseList.value = resp.toList()
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }
}