package com.example.pokedex.repository.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pokedex.repository.database.model.TypeEntity

/**
 * TypeDAO
 *
 * DAO for the Types table in the database. It contains all the queries that can be done on the
 * Types table. It also contains the methods to insert, update and delete a TypeEntity.
 *
 */
@Dao
interface TypeDAO {

    /**
     * Insert a TypeEntity in the database.
     *
     * @param t TypeEntity to insert.
     * @return id of the inserted TypeEntity.
     */
    @Insert
    fun insert(t: TypeEntity): Long

    /**
     * Update a TypeEntity in the database.
     *
     * @param t TypeEntity to update.
     * @return number of rows updated.
     */
    @Update
    fun update(t: TypeEntity): Int

    /**
     * Delete a TypeEntity in the database.
     *
     * @param t TypeEntity to delete.
     * @return number of rows deleted.
     */
    @Delete
    fun delete(t: TypeEntity)

    /**
     * Get all the TypeEntity in the database.
     *
     * @return list of all the TypeEntity in the database.
     */
    @Query("SELECT * FROM Types")
    fun getAll(): List<TypeEntity>

    /**
     * Get a TypeEntity by its id.
     *
     * @param id id of the TypeEntity to get.
     * @return TypeEntity with the given id.
     */
    @Query("SELECT * FROM Types WHERE id = :id")
    fun getById(id: Int): TypeEntity?

    /**
     * Get a TypeEntity by its name.
     *
     * @param name name of the TypeEntity to get.
     * @return TypeEntity with the given name.
     */
    @Query("SELECT * FROM Types WHERE name = :name")
    fun getByName(name: String): TypeEntity?
}