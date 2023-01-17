package com.example.pokedex.repository.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pokedex.repository.database.model.TypeEntity

@Dao
interface TypeDAO {
    @Insert
    fun insert(t: TypeEntity): Long

    @Update
    fun update(t: TypeEntity): Int

    @Delete
    fun delete(t: TypeEntity)

    @Query("SELECT * FROM Types")
    fun getAll(): List<TypeEntity>

    @Query("SELECT * FROM Types WHERE id = :id")
    fun getById(id: Int): TypeEntity?
}