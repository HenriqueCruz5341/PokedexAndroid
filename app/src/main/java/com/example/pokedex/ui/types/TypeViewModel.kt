package com.example.pokedex.ui.types

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.repository.database.client.ClientDatabase
import com.example.pokedex.repository.database.dto.TypeMultiplierDTO
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.repository.database.model.TypeRelationEntity
import com.example.pokedex.utils.Constants

/**
 * TypeViewModel is the ViewModel of the TypeFragment.
 *
 * It is responsible for loading the type and type relation from the database.
 *
 * @property typeList the list of pokemon types.
 * @property typeDefenseList the list of pokemon types and its multipliers as if the selected type
 * is a defender pokemon.
 * @property typeAttackList the list of pokemon types and its multipliers as if the selected type
 * is a attacker pokemon.
 * @property selectedTypes mutable list of the types that are current selected, max size is 2.
 * @property selectedTypesList the limit of selected types to update to the TypeFragment.
 * @property dbTypes TypeDAO database.
 * @property dbRelation TypeRelationDAO database.
 */
class TypeViewModel(application: Application) : AndroidViewModel(application) {
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

    /**
     * This method returns a LiveData of the typeList.
     *
     * @return LiveData of the typeList, a List of TypeEntity.
     */
    fun getTypeList(): LiveData<List<TypeEntity>> {
        return typeList
    }

    /**
     * This method returns a LiveData of the typeAttackList.
     *
     * @return LiveData of the typeAttackList, a List of TypeMultiplierDTO.
     */
    fun getTypeAttackList(): LiveData<List<TypeMultiplierDTO>> {
        return typeAttackList
    }

    /**
     * This method returns a LiveData of the typeDefenseList.
     *
     * @return LiveData of the typeDefenseList, a List of TypeMultiplierDTO.
     */
    fun getTypeDefenseList(): LiveData<List<TypeMultiplierDTO>> {
        return typeDefenseList
    }

    /**
     * This method returns a LiveData of the selectedTypesList.
     *
     * @return LiveData of the selectedTypesList, a List of TypeEntity.
     */
    fun getSelectedTypeList(): LiveData<List<TypeEntity>> {
        return selectedTypesList
    }

    /**
     * This method load all pokemon types from the database to the typeList.
     */
    fun getAllTypes() {
        val db = ClientDatabase.getDatabase(getApplication()).TypeDAO()
        try {
            val resp = db.getAll()
            if (resp.isEmpty()) {
                listMsg.value = Constants.DB_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.DB_MSGS.SUCCESS
                typeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.DB_MSGS.FAIL
        }
    }

    /**
     * This method is called when user select a pokemon type.
     *
     * It update the selectedTypesList, typeAttackList and typeDefenseList.
     * If user select a type that was not previously selected, it is added to the selectedTypesList,
     * but only if selectedTypes size is less than 2, if size is 2, no types are added.
     * If user select an already selected type, that type is deselected and removed from
     * selectedTypes.
     *
     * @param type pokemon type that user selected
     */
    fun userSelectType(type: TypeEntity) {
        if (selectedTypes.isNotEmpty()) {
            if (type in selectedTypes) {
                selectedTypes.remove(type)
                getAllAttack()
                getAllDefense()
                selectedTypesList.value = selectedTypes.toList()
                return
            }
            if (selectedTypes.size >= 2) return
        }
        selectedTypes.add(type)
        getAllAttack()
        getAllDefense()
        selectedTypesList.value = selectedTypes.toList()
    }

    /**
     * This method update the typeAttackList.
     *
     * It calls the general getAll method to update the list with the new values depending on the
     * selected types.
     */
    private fun getAllAttack() {
        getAll(typeAttackList, { dbRelation.getAttack(it) }, { dbTypes.getById(it.defense_id) })
    }

    /**
     * This method update the typeDefenseList.
     *
     * It calls the general getAll method to update the list with the new values depending on the
     * selected types.
     */
    private fun getAllDefense() {
        getAll(typeDefenseList, { dbRelation.getDefense(it) }, { dbTypes.getById(it.attack_id) })
    }

    // TODO refactor
    /**
     * This method update a TypeMultiplierDTO list with its respective values from database.
     *
     * It get a list of TypeRelationEntity with the selected pokemon id, same is done if a second
     * pokemon type was selected. It add the attack/defense pokemon with the found multiplier to a
     * TypeMultiplierDTO list, than if a second pokemon type is selected it adds the additional
     * multipliers or change the multiplier if the type is in the list, than update the listToChange
     * with the resultant value.
     *
     * @param listToChange list of TypeMultiplierDTO to be updated with the new values
     * @param typeFunctionFirst callback function to get a list of TypeRelationEntity with a given id
     * @param typeFunctionSecond callback function to get a TypeEntity with a given id
     */
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
                listMsg.value = Constants.DB_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.DB_MSGS.SUCCESS
                attack.forEach {
                    val type = typeFunctionSecond.invoke(it)
                    if (type != null)
                        resp.add(TypeMultiplierDTO(type.name, it.multiplaier))
                }

                if (attackSecondType != null) {
                    attackSecondType.forEach {
                        val type = typeFunctionSecond.invoke(it)
                        if (type != null) {
                            val aux = resp.find {it2 -> it2.name == type.name }
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
            listMsg.value = Constants.DB_MSGS.FAIL
        }
    }
}
