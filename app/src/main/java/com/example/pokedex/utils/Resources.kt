package com.example.pokedex.utils

import com.example.pokedex.R

class Resources {
    companion object {
        fun getColorByName(type: String): Int {
            return when (type) {
                "normal" -> R.color.normal_type
                "fire" -> R.color.fire_type
                "water" -> R.color.water_type
                "electric" -> R.color.electric_type
                "grass" -> R.color.grass_type
                "ice" -> R.color.ice_type
                "fighting" -> R.color.fighting_type
                "poison" -> R.color.poison_type
                "ground" -> R.color.ground_type
                "flying" -> R.color.flying_type
                "psychic" -> R.color.psychic_type
                "bug" -> R.color.bug_type
                "rock" -> R.color.rock_type
                "ghost" -> R.color.ghost_type
                "dragon" -> R.color.dragon_type
                "dark" -> R.color.dark_type
                "steel" -> R.color.steel_type
                "fairy" -> R.color.fairy_type
                else -> R.color.white
            }
        }

        fun getStringByName(name: String): Int {
            return when (name) {
                "normal" -> R.string.normal
                "fire" -> R.string.fire
                "water" -> R.string.water
                "electric" -> R.string.electric
                "grass" -> R.string.grass
                "ice" -> R.string.ice
                "fighting" -> R.string.fighting
                "poison" -> R.string.poison
                "ground" -> R.string.ground
                "flying" -> R.string.flying
                "psychic" -> R.string.psychic
                "bug" -> R.string.bug
                "rock" -> R.string.rock
                "ghost" -> R.string.ghost
                "dragon" -> R.string.dragon
                "dark" -> R.string.dark
                "steel" -> R.string.steel
                "fairy" -> R.string.fairy
                else -> R.string.unknown
            }
        }
    }
}