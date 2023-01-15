package com.example.pokedex.repository.database.client

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.repository.database.dao.PokemonPageableDAO
import com.example.pokedex.repository.database.model.PokemonPageableEntity

@Database(entities = [PokemonPageableEntity::class], version = 1)
abstract class ClientDatabase : RoomDatabase() {

    abstract fun PokemonPageableDAO(): PokemonPageableDAO

    companion object {
        private lateinit var INSTANCE: ClientDatabase
        fun getDatabase(context: Context): ClientDatabase {

            if(!::INSTANCE.isInitialized) {

                synchronized(ClientDatabase::class) {

                    INSTANCE = Room.databaseBuilder(context, ClientDatabase::class.java, "mydatabase.db")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}