package com.example.pokedex.utils

class Constants {
    object API {
        val BASE_URL_POKEAPI = "https://pokeapi.co/api/v2/"
        val URL_IMAGES_POKEMON = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{{id}}.png"
    }

    object BD_MSGS {
        val SUCCESS = 1
        val FAIL = 0
        val CONSTRAINT = 2
        val NOT_FOUND = 3
    }
}