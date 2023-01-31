package com.example.pokedex.utils

import org.json.JSONObject

/**
 * Constants used in the application.
 *
 * This class contains all the constants used in the application, with no need to create a new object.
 * This class is used to avoid the use of magic numbers and strings.
 *
 */
class Constants {
    /**
     * This object contains all the constants related to the API.
     */
    object API {
        val BASE_URL_POKEAPI = "https://pokeapi.co/api/v2/"
        val URL_IMAGES_POKEMON = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{{id}}.png"
    }

    /**
     * This object contains all the constants related to the database.
     */
    object BD {
        val BD_NAME = "pokedex.db"
    }

    /**
     * This object contains all the constants related to the database massages.
     */
    object DB_MSGS {
        val SUCCESS = 0
        val FAIL = 1
        val CONSTRAINT = 2
        val NOT_FOUND = 3
    }

    /**
     * This object contains all the constants related to the API massages.
     */
    object API_MSGS {
        val SUCCESS = 4
        val FAIL = 5
        val NOT_FOUND = 6
    }

    /**
     * This object contains all the constants related to resources that application uses.
     */
    object RES_MSGS {
        val POKEMON = 0
        val TYPE = 1
        val TYPE_REL = 2
        val SPECIE = 3
        val EVOLUTION = 4
        val REGION = 5
        val LOCATION = 6
        val AREA = 7
    }

    /**
     * This object contains all the constants related to pokemon types.
     */
    object TYPES {
        val listTypes: List<String> = listOf("normal", "fighting", "flying", "poison", "ground",
            "rock", "bug", "ghost", "steel", "fire", "water", "grass", "electric", "psychic", "ice",
            "dragon", "dark", "fairy")

        private val typeChartJsonString = """{types:[{"name":"Normal","immunes":["Ghost"],"weaknesses":["Rock","Steel"],"strengths":[]},
            {"name":"Fire","immunes":[],"weaknesses":["Fire","Water","Rock","Dragon"],"strengths":["Grass","Ice","Bug","Steel"]},
            {"name":"Water","immunes":[],"weaknesses":["Water","Grass","Dragon"],"strengths":["Fire","Ground","Rock"]},
            {"name":"Electric","immunes":["Ground"],"weaknesses":["Electric","Grass","Dragon"],"strengths":["Water","Flying"]},
            {"name":"Grass","immunes":[],"weaknesses":["Fire","Grass","Poison","Flying","Bug","Dragon","Steel"],"strengths":["Water","Ground","Rock"]},
            {"name":"Ice","immunes":[],"weaknesses":["Fire","Water","Ice","Steel"],"strengths":["Grass","Ground","Flying","Dragon"]},
            {"name":"Fighting","immunes":["Ghost"],"weaknesses":["Poison","Flying","Psychic","Bug","Fairy"],"strengths":["Normal","Ice","Rock","Dark","Steel"]},
            {"name":"Poison","immunes":["Steel"],"weaknesses":["Poison","Ground","Rock","Ghost"],"strengths":["Grass","Fairy"]},
            {"name":"Ground","immunes":["Flying"],"weaknesses":["Grass","Bug"],"strengths":["Fire","Electric","Poison","Rock","Steel"]},
            {"name":"Flying","immunes":[],"weaknesses":["Electric","Rock","Steel"],"strengths":["Grass","Fighting","Bug"]},
            {"name":"Psychic","immunes":["Dark"],"weaknesses":["Psychic","Steel"],"strengths":["Fighting","Poison"]},
            {"name":"Bug","immunes":[],"weaknesses":["Fire","Fighting","Poison","Flying","Ghost","Steel","Fairy"],"strengths":["Grass","Psychic","Dark"]},
            {"name":"Rock","immunes":[],"weaknesses":["Fighting","Ground","Steel"],"strengths":["Fire","Ice","Flying","Bug"]},
            {"name":"Ghost","immunes":["Normal"],"weaknesses":["Dark"],"strengths":["Psychic","Ghost"]},
            {"name":"Dragon","immunes":["Fairy"],"weaknesses":["Steel"],"strengths":["Dragon"]},
            {"name":"Dark","immunes":[],"weaknesses":["Fighting","Dark","Fairy"],"strengths":["Psychic","Ghost"]},
            {"name":"Steel","immunes":[],"weaknesses":["Fire","Water","Electric","Steel"],"strengths":["Ice","Rock","Fairy"]},
            {"name":"Fairy","immunes":[],"weaknesses":["Fire","Poison","Steel"],"strengths":["Fighting","Dragon","Dark"]}]}"""

        var typeJsonArray = JSONObject(typeChartJsonString).getJSONArray("types")
    }

    /**
     * This object contains all the constants related to pokemons genders.
     */
    object GENDERS {
        val MALE = 0
        val FEMALE = 1
        val GENDERLESS = 2
    }
}
