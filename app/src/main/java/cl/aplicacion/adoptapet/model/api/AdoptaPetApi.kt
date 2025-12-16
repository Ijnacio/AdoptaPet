package cl.aplicacion.adoptapet.model.api

import cl.aplicacion.adoptapet.model.entities.Mascota
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val correo: String, val password: String)
data class RegistroRequest(val nombre: String, val correo: String, val password: String)

data class UsuarioResponse(val id: Int, val nombre: String, val correo: String)
interface AdoptaPetApi {

    @GET("mascota")
    suspend fun getMascotas(): Response<List<Mascota>>

    // 2. Subir una nueva mascota
    @POST("mascota")
    suspend fun crearMascota(@Body mascota: Mascota): Response<Mascota>


    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioResponse>

    @POST("registro")
    suspend fun registro(@Body request: RegistroRequest): Response<UsuarioResponse>

    @DELETE("mascota/{id}")
    suspend fun eliminarMascota(@Path("id") id: Int): Response<Unit>


}
