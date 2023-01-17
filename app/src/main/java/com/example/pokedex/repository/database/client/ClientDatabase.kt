package com.example.pokedex.repository.database.client

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.repository.database.dao.PokemonPageableDAO
import com.example.pokedex.repository.database.dao.TypeDAO
import com.example.pokedex.repository.database.dao.TypeRelationDAO
import com.example.pokedex.repository.database.model.PokemonPageableEntity
import com.example.pokedex.repository.database.model.TypeEntity
import com.example.pokedex.repository.database.model.TypeRelationEntity

@Database(entities = [PokemonPageableEntity::class, TypeEntity::class, TypeRelationEntity::class], version = 1)
abstract class ClientDatabase : RoomDatabase() {

    abstract fun PokemonPageableDAO(): PokemonPageableDAO

    abstract fun TypeDAO(): TypeDAO

    abstract fun TypeRelationDAO(): TypeRelationDAO

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