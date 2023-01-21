package com.example.pokedex.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="TypeRelations", indices = [Index(value = ["attack_id", "defense_id"], unique = true)], primaryKeys = ["attack_id", "defense_id"])
class TypeRelationEntity {
    @ColumnInfo(name="attack_id")
    var attack_id: Int = 0

    @ColumnInfo(name="defense_id")
    var defense_id: Int = 0

    @ColumnInfo(name="damage_multiplier")
    var multiplaier: Float = 0f
}