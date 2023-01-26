package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.EvolutionEntity

@Dao
interface EvolutionDAO {

    @Insert
    fun insert(e: EvolutionEntity): Long

    @Update
    fun update(e: EvolutionEntity): Int

    @Delete
    fun delete(e: EvolutionEntity)

    @Query("SELECT * FROM Evolution")
    fun getAll(): List<EvolutionEntity>

    @Query("SELECT * FROM Evolution WHERE id = :id")
    fun getById(id: Int): EvolutionEntity?

}