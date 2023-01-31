package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.VarietyEntity

/**
 * VarietyDAO
 *
 * DAO for the Variety table in the database. It contains all the queries that can be done on the
 * Variety table. It also contains the methods to insert, update and delete a VarietyEntity.
 *
 */
@Dao
interface VarietyDAO {

    /**
     * Insert a VarietyEntity in the database.
     *
     * @param v VarietyEntity to insert.
     * @return id of the inserted VarietyEntity.
     */
    @Insert
    fun insert(v: VarietyEntity): Long

    /**
     * Update a VarietyEntity in the database.
     *
     * @param v VarietyEntity to update.
     * @return number of rows updated.
     */
    @Update
    fun update(v: VarietyEntity): Int

    /**
     * Delete a VarietyEntity in the database.
     *
     * @param v VarietyEntity to delete.
     * @return number of rows deleted.
     */
    @Delete
    fun delete(v: VarietyEntity)

    /**
     * Get all the VarietyEntity in the database.
     *
     * @return list of all the VarietyEntity in the database.
     */
    @Query("SELECT * FROM Variety")
    fun getAll(): List<VarietyEntity>

    /**
     * Get a VarietyEntity by its id.
     *
     * @param id id of the VarietyEntity to get.
     * @return VarietyEntity with the given id.
     */
    @Query("SELECT * FROM Variety WHERE id = :id")
    fun getById(id: Int): VarietyEntity?

    /**
     * Get a list of VarietyEntity by their pokemon_id.
     *
     * @param pokemonId pokemon_id of the VarietyEntity to get.
     * @return list of VarietyEntity with the given pokemon_id.
     */
    @Query("SELECT * FROM Variety WHERE pokemon_id = :pokemonId")
    fun getByPokemonId(pokemonId: Int): List<VarietyEntity?>

}