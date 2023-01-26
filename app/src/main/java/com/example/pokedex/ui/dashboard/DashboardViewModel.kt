package com.example.pokedex.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.repository.database.model.TypeRelationEntity
import com.example.pokedex.utils.Constants

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private var listMsg = MutableLiveData<Int>()
    private var typeList = MutableLiveData<List<TypeEntity>>()
    private var typeDefenseList = MutableLiveData<List<TypeMultiplierDTO>>()
    private var typeAttackList = MutableLiveData<List<TypeMultiplierDTO>>()
    private var selectedTypes: MutableList<TypeEntity> = mutableListOf()
    private var selectedTypesList = MutableLiveData<List<TypeEntity>>()

    private val dbTypes = ClientDatabase.getDatabase(getApplication()).TypeDAO()
    private val dbRelation = ClientDatabase.getDatabase(getApplication()).TypeRelationDAO()

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

    fun getSelectedTypeList(): LiveData<List<TypeEntity>> {
        return selectedTypesList
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

    fun userSelectType(type: TypeEntity) {
        if (selectedTypes.isNotEmpty()) {
            if (type in selectedTypes) {
                selectedTypes.remove(type)
                getAllEffectiveness()
                getAllWeakness()
                selectedTypesList.value = selectedTypes.toList()
                return
            }
            if (selectedTypes.size >= 2) return
        }
        selectedTypes.add(type)
        getAllEffectiveness()
        getAllWeakness()
        selectedTypesList.value = selectedTypes.toList()
    }

    private fun getAll(listToChange: MutableLiveData<List<TypeMultiplierDTO>>, typeFunctionFirst: (selectId: Int)->List<TypeRelationEntity>, typeFunctionSecond: (typeRelation: TypeRelationEntity) -> TypeEntity?) {
        if(selectedTypes.isEmpty()) {
            listToChange.value = listOf()
            return
        }
        try {
            val attack = typeFunctionFirst.invoke(selectedTypes[0].id)
            var attackSecondType: List<TypeRelationEntity>? = null
            if (selectedTypes.size == 2) attackSecondType = typeFunctionFirst.invoke(selectedTypes[1].id)
            var resp: MutableList<TypeMultiplierDTO> = mutableListOf()
            if (attack.isEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                attack.forEach {
                    val type = typeFunctionSecond.invoke(it)
                    if (type != null)
                        resp.add(TypeMultiplierDTO(type.name, it.multiplaier))
                }
                if (attackSecondType != null) {
                    attackSecondType.forEach {
                        val type = typeFunctionSecond.invoke(it)
                        if (type != null) {
                            val aux = resp.find { it.name == type.name }
                            if (aux != null)
                                aux.multiplier *= it.multiplaier
                            else
                                resp.add(TypeMultiplierDTO(type.name, it.multiplaier))
                        }
                    }
                }
                listToChange.value = resp.toList()
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    private fun getAllEffectiveness() {
        getAll(typeAttackList, { dbRelation.getAttack(it) }, { dbTypes.getById(it.defense_id) })
    }

    private fun getAllWeakness() {
        getAll(typeDefenseList, { dbRelation.getDefense(it) }, { dbTypes.getById(it.attack_id) })
    }
}
