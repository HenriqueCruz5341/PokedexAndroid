package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.PokemonPageableEntity

/**
 * PokemonPageableDAO
 *
 * DAO for the PokemonPageable table in the database. It contains all the queries that can be done on the
 * PokemonPageable table. It also contains the methods to insert, update and delete a PokemonPageableEntity.
 *
 */
@Dao
interface PokemonPageableDAO {

    /**
     * Insert a PokemonPageableEntity in the database.
     *
     * @param p PokemonPageableEntity to insert.
     * @return id of the inserted PokemonPageableEntity.
     */
    @Insert
    fun insert(p: PokemonPageableEntity): Long

    /**
     * Update a PokemonPageableEntity in the database.
     *
     * @param p PokemonPageableEntity to update.
     * @return number of rows updated.
     */
    @Update
    fun update(p: PokemonPageableEntity): Int

    /**
     * Delete a PokemonPageableEntity in the database.
     *
     * @param p PokemonPageableEntity to delete.
     * @return number of rows deleted.
     */
    @Delete
    fun delete(p: PokemonPageableEntity)

    /**
     * Get all the PokemonPageableEntity in the database.
     *
     * @return list of all the PokemonPageableEntity in the database.
     */
    @Query("SELECT * FROM PokemonPageable")
    fun getAll(): List<PokemonPageableEntity>

    /**
     * Get a PokemonPageableEntity with pagination.
     *
     * @param offset offset of the PokemonPageableEntity to get.
     * @param limit limit of the PokemonPageableEntity to get.
     * @return PokemonPageableEntity with the given offset and limit.
     */
    @Query("SELECT * FROM PokemonPageable ORDER BY id LIMIT :limit OFFSET :offset")
    fun getPagination(offset: Int, limit: Int): List<PokemonPageableEntity>

    /**
     * Get a PokemonPageableEntity by its id.
     *
     * @param id id of the PokemonPageableEntity to get.
     * @return PokemonPageableEntity with the given id.
     */
    @Query("SELECT * FROM PokemonPageable WHERE id = :id")
    fun getById(id: Int): PokemonPageableEntity?

    /**
     * Get a PokemonPageableEntity by its name with like operator.
     *
     * @param name name of the PokemonPageableEntity to get.
     * @return PokemonPageableEntity with the given name.
     */
    @Query("SELECT * FROM PokemonPageable WHERE name LIKE :name")
    fun getByName(name: String): List<PokemonPageableEntity?>

}