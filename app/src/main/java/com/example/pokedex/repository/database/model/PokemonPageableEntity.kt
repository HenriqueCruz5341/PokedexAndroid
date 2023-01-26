package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="PokemonPageable", indices = [Index(value = ["id"], unique = true)])
class PokemonPageableEntity {
    @PrimaryKey()
    @ColumnInfo(name="id")
    var id: Int = 0

    @ColumnInfo(name="name")
    var name: String = ""

    @ColumnInfo(name="url")
    var url: String = ""

    @ColumnInfo(name="image")
    var image: String = ""

    @ColumnInfo(name="count")
    var count: Int = 0
}