package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * TypeEntity
 *
 * This class is used to define the Types table in the database. It is used to store a pokemon
 * types. This is used for type damage calculation.
 *
 * @property id primary key, type unique identifier.
 * @property name the name of the type.
 */
@Entity(tableName="Types", indices = [Index(value = ["id"], unique = true)])
class TypeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int = 0

    @ColumnInfo(name="name")
    var name: String = ""
}