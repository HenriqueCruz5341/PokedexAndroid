package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * VarietyEntity
 *
 * This class is used to define the Variety table in the database. It is used to store the different
 * varieties of a pokemon. Variety is a table that contains all the different forms of a Pokemon.
 * Like Exeggutor and Exeggutor Alola, but both are the same Pokemon Specie.
 *
 * @property id primary key.
 * @property pokemonId the id of the pokemon and it is the same in the Pokemon table. Many varieties
 * can have the same pokemon id, because all are the same pokemon specie.
 * @property isDefault if the pokemon is the default form.
 * @property pokemonName the name of the pokemon.
 *
 */
@Entity(
    tableName = "Variety",
    indices = [Index(value = ["id"], unique = true), Index(
        value = ["pokemon_id"],
        unique = false
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