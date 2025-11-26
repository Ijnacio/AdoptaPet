package cl.aplicacion.adoptapet.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:n0ZLeXqG/"


    val api: AdoptaPetApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdoptaPetApi::class.java)
    }
}