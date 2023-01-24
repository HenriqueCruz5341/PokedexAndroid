package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Variety",
    indices = [Index(value = ["id"], unique = true), Index(
        value = ["pokemon_id"],
        unique = true
    )]
)
class VarietyEntity {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "pokemon_id")
    var pokemonId: Int = 0

    @ColumnInfo(name = "is_default")
    var isDefault: Boolean = false

    @ColumnInfo(name = "pokemon_name")
    var pokemonName: String = ""

}