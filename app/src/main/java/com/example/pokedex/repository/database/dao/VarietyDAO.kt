package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.VarietyEntity

@Dao
interface VarietyDAO {

    @Insert
    fun insert(v: VarietyEntity): Long

    @Update
    fun update(v: VarietyEntity): Int

    @Delete
    fun delete(v: VarietyEntity)

    @Query("SELECT * FROM Variety")
    fun getAll(): List<VarietyEntity>

    @Query("SELECT * FROM Variety WHERE id = :id")
    fun getById(id: Int): VarietyEntity?

    @Query("SELECT * FROM Variety WHERE pokemon_id = :pokemonId")
    fun getByPokemonId(pokemonId: Int): List<VarietyEntity?>

}