package com.example.pokedex.utils

import com.example.pokedex.R

/**
 * A Resources class used to get resources.
 *
 * This class is used to get some resources that are used in the app like colors and strings,
 * with no need to create a new instance of the class. It is used to avoid the repetition of code, and
 * to make the code more readable.
 */
class Resources {
    companion object {

        /**
         * This function is used to get the color of a pokemon type by its name
         *
         * @param type The name of the type
         * @return The color of the type
         */
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

        /**
         * This function is used to get the string of a pokemon type by its name, very useful for
         * internationalization.
         *
         * @param name The name of the type
         * @return The string of the type
         */
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

        /**
         * This function is used to get the error message of a status message.
         *
         * @param statusMessage The status message to get the error message
         * @return The error message
         */
        fun getErrorMessageByStatusMessage(statusMessage: StatusMessage): Int {
            when (statusMessage.resource){
                Constants.RES_MSGS.POKEMON -> {
                    return when (statusMessage.code){
                        Constants.API_MSGS.FAIL -> R.string.error_loading_pokemons
                        Constants.API_MSGS.NOT_FOUND -> R.string.error_not_found_pokemon
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_pokemon
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_pokemon
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.TYPE -> {
                    return when (statusMessage.code){
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_types
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_types
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.TYPE_REL -> {
                    return when (statusMessage.code){
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_type_relations
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_type_relations
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.SPECIE -> {
                    return when (statusMessage.code){
                        Constants.API_MSGS.NOT_FOUND -> R.string.error_not_found_specie
                        Constants.API_MSGS.FAIL -> R.string.error_loading_species
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_specie
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_specie
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.EVOLUTION -> {
                    return when (statusMessage.code){
                        Constants.API_MSGS.NOT_FOUND -> R.string.error_not_found_evolution
                        Constants.API_MSGS.FAIL -> R.string.error_loading_evolutions
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_evolution
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_evolution
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.REGION -> {
                    return when (statusMessage.code){
                        Constants.API_MSGS.NOT_FOUND -> R.string.error_not_found_region
                        Constants.API_MSGS.FAIL -> R.string.error_loading_regions
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_region
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_region
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.LOCATION -> {
                    return when (statusMessage.code){
                        Constants.API_MSGS.NOT_FOUND -> R.string.error_not_found_location
                        Constants.API_MSGS.FAIL -> R.string.error_loading_locations
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_location
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_location
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                Constants.RES_MSGS.AREA -> {
                    return when (statusMessage.code){
                        Constants.API_MSGS.NOT_FOUND -> R.string.error_not_found_area
                        Constants.API_MSGS.FAIL -> R.string.error_loading_areas
                        Constants.DB_MSGS.CONSTRAINT -> R.string.error_saving_area
                        Constants.DB_MSGS.NOT_FOUND -> R.string.error_not_found_area
                        Constants.DB_MSGS.FAIL -> R.string.error_db
                        else -> R.string.error_unmapped
                    }
                }
                else -> return R.string.error_unmapped
            }
        }
    }
}