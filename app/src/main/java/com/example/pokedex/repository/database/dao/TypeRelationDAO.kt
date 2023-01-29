package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.dto.TypeRelationDTO
import com.example.pokedex.repository.database.model.TypeRelationEntity

@Dao
interface TypeRelationDAO {
    @Insert
    fun insert(t: TypeRelationEntity): Long

    @Update
    fun update(t: TypeRelationEntity): Int

    @Delete
    fun delete(t: TypeRelationEntity)

    @Query("SELECT * FROM TypeRelations")
    fun getAll(): List<TypeRelationEntity>

    @Query("SELECT * FROM TypeRelations WHERE defense_id = :defense_id")
    fun getByDefenseId(defense_id: Int): List<TypeRelationEntity>

    @Query("SELECT * FROM TypeRelations WHERE attack_id = :attack_id")
    fun getByAttackId(attack_id: Int): List<TypeRelationEntity>

    @Query("SELECT * FROM TypeRelations WHERE attack_id = :attack_id AND defense_id = :defense_id")
    fun getByAttackAndDefenseId(attack_id: Int, defense_id: Int): TypeRelationEntity?

    @Query("SELECT * FROM TypeRelations WHERE attack_id = :id")
    fun getAttack(id: Int): List<TypeRelationEntity>

    @Query("SELECT * FROM TypeRelations WHERE defense_id = :id")
    fun getDefense(id: Int): List<TypeRelationEntity>

    @Query("SELECT t.name AS attack, t2.name AS defense, tr.damage_multiplier AS damage_multiplier " +
            "FROM Types AS t INNER JOIN TypeRelations AS tr ON t.id = tr.attack_id INNER JOIN Types AS t2 ON tr.defense_id = t2.id " +
            "WHERE t2.name = :typeOne OR t2.name = :typeTwo ORDER BY t.name")
    fun getByTypesName(typeOne: String, typeTwo: String): List<TypeRelationDTO>
}