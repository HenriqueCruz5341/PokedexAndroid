package com.example.pokedex.repository.database.client

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.repository.database.dao.*
import com.example.pokedex.repository.database.model.*

@Database(
    entities = [PokemonPageableEntity::class, TypeEntity::class,
        TypeRelationEntity::class, PokemonEntity::class,
        VarietyEntity::class, EvolutionEntity::class],
    version = 16
)
abstract class ClientDatabase : RoomDatabase() {

    abstract fun PokemonPageableDAO(): PokemonPageableDAO
    abstract fun PokemonDAO(): PokemonDAO
    abstract fun TypeDAO(): TypeDAO
    abstract fun TypeRelationDAO(): TypeRelationDAO
    abstract fun VarietyDAO(): VarietyDAO
    abstract fun EvolutionDAO(): EvolutionDAO

    companion object {
        private lateinit var INSTANCE: ClientDatabase
        fun getDatabase(context: Context): ClientDatabase {

            if (!::INSTANCE.isInitialized) {

                synchronized(ClientDatabase::class) {

                    INSTANCE =
                        Room.databaseBuilder(context, ClientDatabase::class.java, "mydatabase.db")
                            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}