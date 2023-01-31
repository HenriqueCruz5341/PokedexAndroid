package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.repository.database.model.VarietyEntity

/**
 * EvolutionDAO
 *
 * DAO for the Evolution table in the database. It contains all the queries that can be done on the
 * Evolution table. It also contains the methods to insert, update and delete an EvolutionEntity.
 *
 */
@Dao
interface EvolutionDAO {

    /**
     * Insert an EvolutionEntity in the database.
     *
     * @param e EvolutionEntity to insert.
     * @return id of the inserted EvolutionEntity.
     */
    @Insert
    fun insert(e: EvolutionEntity): Long

    /**
     * Update an EvolutionEntity in the database.
     *
     * @param e EvolutionEntity to update.
     * @return number of rows updated.
     */
    @Update
    fun update(e: EvolutionEntity): Int

    /**
     * Delete an EvolutionEntity in the database.
     *
     * @param e EvolutionEntity to delete.
     * @return number of rows deleted.
     */
    @Delete
    fun delete(e: EvolutionEntity)

    /**
     * Get all the EvolutionEntity in the database.
     *
     * @return list of all the EvolutionEntity in the database.
     */
    @Query("SELECT * FROM Evolution")
    fun getAll(): List<EvolutionEntity>

    /**
     * Get an EvolutionEntity by its id.
     *
     * @param id id of the EvolutionEntity to get.
     * @return EvolutionEntity with the given id.
     */
    @Query("SELECT * FROM Evolution WHERE id = :id")
    fun getById(id: Int): EvolutionEntity?

    /**
     * Get an EvolutionEntity by its chain.
     *
     * @param chain chain of the EvolutionEntity to get.
     * @return EvolutionEntity with the given chain.
     */
    @Query("SELECT * \n" +
            "FROM Evolution e\n" +
            "WHERE e.chain = (\n" +
            "   SELECT e2.chain FROM Evolution e2 WHERE e2.id = :pokemonId\n" +
            ")")
    fun getChainByPokemonId(pokemonId: Int): List<EvolutionEntity?>
}