package com.example.pokedex.repository.database.dao

import androidx.room.*
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

    @Query("SELECT * FROM TypeRelations WHERE atack_id = :atack_id")
    fun getByAtackId(atack_id: Int): List<TypeRelationEntity>

    @Query("SELECT * FROM TypeRelations WHERE atack_id = :atack_id AND defense_id = :defense_id")
    fun getByAtackAndDefenseId(atack_id: Int, defense_id: Int): TypeRelationEntity?
}