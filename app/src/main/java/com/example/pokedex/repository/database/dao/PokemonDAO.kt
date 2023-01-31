package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.PokemonEntity

/**
 * PokemonDAO
 *
 * DAO for the Pokemon table in the database. It contains all the queries that can be done on the
 * Pokemon table. It also contains the methods to insert, update and delete a PokemonEntity.
 *
 */
@Dao
interface PokemonDAO {

    /**
     * Insert a PokemonEntity in the database.
     *
     * @param p PokemonEntity to insert.
     * @return id of the inserted PokemonEntity.
     */
    @Insert
    fun insert(p: PokemonEntity): Long

    /**
     * Update a PokemonEntity in the database.
     *
     * @param p PokemonEntity to update.
     * @return number of rows updated.
     */
    @Update
    fun update(p: PokemonEntity): Int

    /**
     * Delete a PokemonEntity in the database.
     *
     * @param p PokemonEntity to delete.
     * @return number of rows deleted.
     */
    @Delete
    fun delete(p: PokemonEntity)

    /**
     * Get all the PokemonEntity in the database.
     *
     * @return list of all the PokemonEntity in the database.
     */
    @Query("SELECT * FROM Pokemon")
    fun getAll(): List<PokemonEntity>

    /**
     * Get a PokemonEntity by its id.
     *
     * @param id id of the PokemonEntity to get.
     * @return PokemonEntity with the given id.
     */
    @Query("SELECT * FROM Pokemon WHERE id = :id")
    fun getById(id: Int): PokemonEntity?
}