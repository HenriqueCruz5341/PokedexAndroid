package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * EvolutionEntity
 *
 * This class is used to define the Evolution table in the database. It is used to store the evolution
 * chain of a pokemon.
 *
 * @property id primary key and are the number of pokemon specie.
 * @property chain used to group the evolutions together.
 * @property order used to determine the order of the evolutions.
 * @property pokemonName the name of the pokemon.
 * @property pokemonImage the image of the pokemon.
 */
@Entity(
    tableName = "Evolution",
    indices = [Index(value = ["id"], unique = true), Index(
        value = ["chain"],
        unique = false
    )]
)
class EvolutionEntity {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "chain")
    var chain: Int = 0

    @ColumnInfo(name = "order")
    var order: Int = 0

    @ColumnInfo(name = "pokemon_name")
    var pokemonName: String = ""

    @ColumnInfo(name = "pokemon_image")
    var pokemonImage: String = ""

}