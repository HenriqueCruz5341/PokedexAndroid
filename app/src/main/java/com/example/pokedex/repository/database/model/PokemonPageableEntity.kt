package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * PokemonPageableEntity
 *
 * This class is used to define the PokemonPageable table in the database. It is used to store the pokemon
 * information. This is used for the pagination of the Pokemon list, because when request a pokemon page only few
 * pokemon information are returned, so we need to store them in the database on a separate table.
 *
 * @property id primary key.
 * @property name the name of the pokemon.
 * @property url the url of the pokemon.
 * @property image the image url of the pokemon.
 * @property count the number of pokemons that are in the API.
 */
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