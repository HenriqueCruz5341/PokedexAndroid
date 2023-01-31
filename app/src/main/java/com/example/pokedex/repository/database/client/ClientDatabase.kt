package com.example.pokedex.repository.database.client

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.repository.database.dao.*
import com.example.pokedex.repository.database.model.*
import com.example.pokedex.utils.Constants

/**
 * A class that represents the database of the application.
 *
 * This class is responsible for creating the database and the DAOs.
 * This class is a singleton, so it can be accessed from anywhere in the application.
 *
 * @property INSTANCE The ClientDatabase instance.
 */
@Database(
    entities = [PokemonPageableEntity::class, TypeEntity::class,
        TypeRelationEntity::class, PokemonEntity::class,
        VarietyEntity::class, EvolutionEntity::class],
    version = 1
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

        /**
         * This function is used to create the database instance.
         *
         * @param context The context of the application.
         * @return The database instance.
         */
        fun getDatabase(context: Context): ClientDatabase {

            if (!::INSTANCE.isInitialized) {

                synchronized(ClientDatabase::class) {

                    INSTANCE =
                        Room.databaseBuilder(context, ClientDatabase::class.java, Constants.BD.BD_NAME)
                            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}