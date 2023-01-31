package com.example.pokedex.repository.api.client

import com.example.pokedex.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class is used to create the retrofit instance.
 *
 * It is used to create the retrofit, and the services, using the Singleton pattern. Only works with
 * the API url in BASE_URL_POKEAPI constant.
 *
 * @property INSTANCE The retrofit instance.
 */
class ClientPokeApi {
    companion object {
        private lateinit var INSTANCE: Retrofit

        /**
         * This function is used to create the retrofit instance.
         *
         * @return The retrofit instance.
         */
        private fun getRetrofitInstance(): Retrofit {
            val http = OkHttpClient.Builder()
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(Constants.API.BASE_URL_POKEAPI)
                    .client(http.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE
        }

        /**
         * This function is used to create the service.
         * @param className The class name of the service.
         * @return The service.
         */
        fun <S> createService(className: Class<S>): S {
            return getRetrofitInstance().create(className)
        }
    }
}