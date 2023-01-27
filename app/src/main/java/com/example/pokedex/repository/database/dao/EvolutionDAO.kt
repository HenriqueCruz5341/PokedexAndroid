package com.example.pokedex.repository.database.dao

import androidx.room.*
import com.example.pokedex.repository.database.model.EvolutionEntity
import com.example.pokedex.repository.database.model.VarietyEntity

@Dao
interface EvolutionDAO {

    @Insert
    fun insert(e: EvolutionEntity): Long

    @Update
    fun update(e: EvolutionEntity): Int

    @Delete
    fun delete(e: EvolutionEntity)

    @Query("SELECT * FROM Evolution")
    fun getAll(): List<EvolutionEntity>

    @Query("SELECT * FROM Evolution WHERE id = :id")
    fun getById(id: Int): EvolutionEntity?

    @Query("SELECT * \n" +
            "FROM Evolution e\n" +
            "WHERE e.chain = (\n" +
            "   SELECT e2.chain FROM Evolution e2 WHERE e2.id = :pokemonId\n" +
            ")")
    fun getChainByPokemonId(pokemonId: Int): List<EvolutionEntity?>
}