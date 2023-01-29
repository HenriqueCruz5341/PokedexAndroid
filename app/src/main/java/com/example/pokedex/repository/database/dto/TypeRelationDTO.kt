package com.example.pokedex.repository.database.dto

import androidx.room.ColumnInfo

class TypeRelationDTO {
    @ColumnInfo(name = "attack")
    var attack: String = ""

    @ColumnInfo(name = "defense")
    var defense: String = ""

    @ColumnInfo(name = "damage_multiplier")
    var damageMultiplier: Float = 0f
}