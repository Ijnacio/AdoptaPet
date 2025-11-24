package cl.aplicacion.adoptapet.model.api

import cl.aplicacion.adoptapet.model.entities.Mascota
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AdoptaPetApi {

    @GET("mascota")
    suspend fun getMascotas(): Response<List<Mascota>>

    // 2. Subir una nueva mascota
    @POST("mascota")
    suspend fun crearMascota(@Body mascota: Mascota): Response<Mascota>
}