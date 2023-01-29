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

    @Query("SELECT * FROM PokemonPageable ORDER BY id LIMIT :limit OFFSET :offset")
    fun getPagination(offset: Int, limit: Int): List<PokemonPageableEntity>

    @Query("SELECT * FROM PokemonPageable WHERE id = :id")
    fun getById(id: Int): PokemonPageableEntity?

    @Query("SELECT * FROM PokemonPageable WHERE name like :name")
    fun getByName(name: String): List<PokemonPageableEntity?>

}