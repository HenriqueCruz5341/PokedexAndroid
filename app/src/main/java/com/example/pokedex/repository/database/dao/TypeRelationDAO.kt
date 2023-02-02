package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.dto.TypeRelationDTO
import com.example.pokedex.repository.database.model.TypeRelationEntity

/**
 * TypeRelationDAO
 *
 * DAO for the TypeRelations table in the database. It contains all the queries that can be done on
 * the TypeRelations table. It also contains the methods to insert, update and delete a
 * TypeRelationEntity.
 *
 */
@Dao
interface TypeRelationDAO {

    /**
     * Insert a TypeRelationEntity in the database.
     *
     * @param t TypeRelationEntity to insert.
     * @return id of the inserted TypeRelationEntity.
     */
    @Insert
    fun insert(t: TypeRelationEntity): Long

    /**
     * Update a TypeRelationEntity in the database.
     *
     * @param t TypeRelationEntity to update.
     * @return number of rows updated.
     */
    @Update
    fun update(t: TypeRelationEntity): Int

    /**
     * Delete a TypeRelationEntity in the database.
     *
     * @param t TypeRelationEntity to delete.
     * @return number of rows deleted.
     */
    @Delete
    fun delete(t: TypeRelationEntity)

    /**
     * Get all the TypeRelationEntity in the database.
     *
     * @return list of all the TypeRelationEntity in the database.
     */
    @Query("SELECT * FROM TypeRelations")
    fun getAll(): List<TypeRelationEntity>

    /**
     * Get all TypeRelationEntity with a defense_id.
     *
     * @param defense_id defense_id of the TypeRelationEntity to get.
     * @return list of the TypeRelationEntity with the given id.
     */
    @Query("SELECT * FROM TypeRelations WHERE defense_id = :defense_id")
    fun getByDefenseId(defense_id: Int): List<TypeRelationEntity>

    /**
     * Get all TypeRelationEntity with a attack_id.
     *
     * @param attack_id attack_id of the TypeRelationEntity to get.
     * @return list of the TypeRelationEntity with the given id.
     */
    @Query("SELECT * FROM TypeRelations WHERE attack_id = :attack_id")
    fun getByAttackId(attack_id: Int): List<TypeRelationEntity>

    /**
     * Get a TypeRelationEntity by its defense and attack id.
     *
     * @param attack_id id of the TypeRelationEntity to get.
     * @param defense_id id of the TypeRelationEntity to get
     * @return TypeRelationEntity with the given ids.
     */
    @Query("SELECT * FROM TypeRelations WHERE attack_id = :attack_id AND defense_id = :defense_id")
    fun getByAttackAndDefenseId(attack_id: Int, defense_id: Int): TypeRelationEntity?

    /**
     * Get a List of TypeRelationEntity by its defense and attack name.
     *
     * @param typeOne name of pokemon type one.
     * @param typeTwo name of pokemon type two.
     * @return List of TypeRelationEntity with the given names.
     */
    @Query("SELECT t.name AS attack, t2.name AS defense, tr.damage_multiplier AS damage_multiplier " +
            "FROM Types AS t INNER JOIN TypeRelations AS tr ON t.id = tr.attack_id INNER JOIN Types AS t2 ON tr.defense_id = t2.id " +
            "WHERE t2.name = :typeOne OR t2.name = :typeTwo ORDER BY t.name")
    fun getByTypesName(typeOne: String, typeTwo: String): List<TypeRelationDTO>
}