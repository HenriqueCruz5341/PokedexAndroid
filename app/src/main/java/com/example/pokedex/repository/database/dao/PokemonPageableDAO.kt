package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.PokemonPageableEntity

@Dao
interface PokemonPageableDAO {

    @Insert
    fun insert(p: PokemonPageableEntity): Long

    @Update
    fun update(p: PokemonPageableEntity): Int

    @Delete
    fun delete(p: PokemonPageableEntity)

    @Query("SELECT * FROM PokemonPageable")
    fun getAll(): List<PokemonPageableEntity>

    @Query("SELECT * FROM PokemonPageable WHERE id = :id")
    fun getById(id: Int): PokemonPageableEntity?

}