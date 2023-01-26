package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.PokemonEntity

@Dao
interface PokemonDAO {

    @Insert
    fun insert(p: PokemonEntity): Long

    @Update
    fun update(p: PokemonEntity): Int

    @Delete
    fun delete(p: PokemonEntity)

    @Query("SELECT * FROM Pokemon")
    fun getAll(): List<PokemonEntity>

    @Query("SELECT * FROM Pokemon WHERE id = :id")
    fun getById(id: Int): PokemonEntity?
}