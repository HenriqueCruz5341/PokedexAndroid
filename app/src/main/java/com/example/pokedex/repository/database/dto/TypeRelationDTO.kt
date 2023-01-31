package com.example.pokedex.repository.database.dto

import androidx.room.ColumnInfo

/**
 * Data Transfer Object for the relation between two types.
 *
 * This class is used to store the relation between two types. It is used when an query is made to the
 * database to get the relation between two types.
 *
 * @param attack The name of the attacking type.
 * @param defense The name of the defending type.
 * @param damageMultiplier The multiplier of the attack.
 */
class TypeRelationDTO {
    @ColumnInfo(name = "attack")
    var attack: String = ""

    @ColumnInfo(name = "defense")
    var defense: String = ""

    @ColumnInfo(name = "damage_multiplier")
    var damageMultiplier: Float = 0f
}