package cl.aplicacion.adoptapet.repository

import cl.aplicacion.adoptapet.model.api.RetrofitClient
import cl.aplicacion.adoptapet.model.api.LoginRequest
import cl.aplicacion.adoptapet.model.api.RegistroRequest
import cl.aplicacion.adoptapet.model.api.UsuarioResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val api = RetrofitClient.api

    suspend fun login(correo: String, pass: String): Result<UsuarioResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequest(correo, pass))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Credenciales inválidas"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun registro(nombre: String, correo: String, pass: String): Result<UsuarioResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.registro(RegistroRequest(nombre, correo, pass))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al registrar"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}