package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * PokemonEntity
 *
 * This class is used to define the Pokemon table in the database. It is used to store the pokemon
 * information.
 *
 * @property id primary key.
 * @property name the name of the pokemon.
 * @property speciesUrl the url of the pokemon species.
 * @property imgDefault the default image of the pokemon.
 * @property imgFemale the female image of the pokemon.
 * @property imgShiny the shiny image of the pokemon.
 * @property imgShinyFemale the shiny female image of the pokemon.
 * @property typeOne the first type of the pokemon.
 * @property typeTwo the second type of the pokemon, if it has one.
 * @property statHp the hp stat of the pokemon.
 * @property statAttack the attack stat of the pokemon.
 * @property statDefense the defense stat of the pokemon.
 * @property statSpAttack the special attack stat of the pokemon.
 * @property statSpDefense the special defense stat of the pokemon.
 * @property statSpeed the speed stat of the pokemon.
 * @property genderRate the gender rate of the pokemon.
 * If -1 genderless, 0 100% male, 8 100% female, else is the rate of female.
 */
@Entity(
    tableName = "Pokemon",
    indices = [Index(value = ["id"], unique = true)]
)
class PokemonEntity {
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "specie_url")
    var speciesUrl: String = ""

    @ColumnInfo(name = "img_default")
    var imgDefault: String = ""

    @ColumnInfo(name = "img_female")
    var imgFemale: String = ""

    @ColumnInfo(name = "img_shiny")
    var imgShiny: String = ""

    @ColumnInfo(name = "img_shiny_female")
    var imgShinyFemale: String = ""

    @ColumnInfo(name = "type_one")
    var typeOne: String = ""

    @ColumnInfo(name = "type_two")
    var typeTwo: String? = null

    @ColumnInfo(name = "stat_hp")
    var statHp: Int = 0

    @ColumnInfo(name = "stat_attack")
    var statAttack: Int = 0

    @ColumnInfo(name = "stat_defense")
    var statDefense: Int = 0

    @ColumnInfo(name = "stat_sp_attack")
    var statSpAttack: Int = 0

    @ColumnInfo(name = "stat_sp_defense")
    var statSpDefense: Int = 0

    @ColumnInfo(name = "stat_speed")
    var statSpeed: Int = 0

    @ColumnInfo(name = "gender_rate")
    var genderRate: Int = 0
}