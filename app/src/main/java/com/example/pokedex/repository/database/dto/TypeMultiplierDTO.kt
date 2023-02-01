package com.example.pokedex.repository.database.dto

/**
 * Data Transfer Object for a relation of type and damage multiplier.
 *
 * This class is used to store the relation between a types and a damage multiplier. It is used when
 * a query is made to the database to get the relation of a type and a damage multiplier.
 *
 * @param name The type name.
 * @param multiplier The damage multiplier to the respective type.
 */
class TypeMultiplierDTO (val name: String, var multiplier: Float) {
}
