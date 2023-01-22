package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="Pokemon", indices = [Index(value = ["id"], unique = true)])
class PokemonEntity {
    @PrimaryKey()
    @ColumnInfo(name="id")
    var id: Int = 0

    @ColumnInfo(name="name")
    var name: String = ""

    @ColumnInfo(name="specie_url")
    var speciesUrl: String = ""

    @ColumnInfo(name="img_default")
    var imgDefault: String = ""

    @ColumnInfo(name="img_female")
    var imgFemale: String = ""

    @ColumnInfo(name="img_shiny")
    var imgShiny: String = ""

    @ColumnInfo(name="img_shiny_female")
    var imgShinyFemale: String = ""

    @ColumnInfo(name="type_one")
    var typeOne: String = ""

    @ColumnInfo(name="type_two")
    var typeTwo: String? = null

    @ColumnInfo(name="stat_hp")
    var statHp: Int = 0

    @ColumnInfo(name="stat_attack")
    var statAttack: Int = 0

    @ColumnInfo(name="stat_defense")
    var statDefense: Int = 0

    @ColumnInfo(name="stat_sp_attack")
    var statSpAttack: Int = 0

    @ColumnInfo(name="stat_sp_defense")
    var statSpDefense: Int = 0

    @ColumnInfo(name="stat_speed")
    var statSpeed: Int = 0

}