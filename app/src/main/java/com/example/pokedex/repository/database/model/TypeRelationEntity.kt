package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// TODO rename properties
/**
 * TypeRelationEntity
 *
 * This class is used to define the TypeRelations table in the database. It is used to store the
 * relation of two pokemon types, and a damage multiplier. This is used for type damage calculation.
 *
 * @property attack_id the id of the attacker type.
 * @property defense_id the if of the defender type.
 * @property multiplaier the damage multiplier of the relation.
 */
@Entity(tableName="TypeRelations", indices = [Index(value = ["attack_id", "defense_id"], unique = true)], primaryKeys = ["attack_id", "defense_id"])
class TypeRelationEntity {
    @ColumnInfo(name="attack_id")
    var attack_id: Int = 0

    @ColumnInfo(name="defense_id")
    var defense_id: Int = 0

    @ColumnInfo(name="damage_multiplier")
    var multiplaier: Float = 0f
}