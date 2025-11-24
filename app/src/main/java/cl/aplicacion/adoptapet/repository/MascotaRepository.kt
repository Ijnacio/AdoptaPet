package cl.aplicacion.adoptapet.repository

import android.util.Log
import cl.aplicacion.adoptapet.model.api.RetrofitClient
import cl.aplicacion.adoptapet.model.dao.MascotaDao
import cl.aplicacion.adoptapet.model.dao.SolicitudDao
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.model.entities.Solicitud
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MascotaRepository(
    private val mascotaDao: MascotaDao,
    private val solicitudDao: SolicitudDao
) {
    // Instancia de nuestra API (Retrofit)
    private val api = RetrofitClient.api

    // --- 1. OBTENER MASCOTAS (Flow para la UI) ---
    fun getAllMascotas(): Flow<List<Mascota>> {
        // Lanzamos la actualización de la nube en segundo plano sin detener la UI
        CoroutineScope(Dispatchers.IO).launch {
            refreshMascotas()
        }
        // Retornamos SIEMPRE la fuente de verdad local (Room)
        return mascotaDao.getAllMascotas()
    }

    // --- 2. OBTENER MASCOTA POR ID ---
    fun getMascotaById(id: Int): Flow<Mascota> {
        return mascotaDao.getMascotaById(id)
    }

    // --- 3. AGREGAR MASCOTA (Lógica Híbrida: Nube + Local) ---
    suspend fun insertarMascota(mascota: Mascota) {
        withContext(Dispatchers.IO) {
            try {
                // A) Intentamos subir a la nube primero
                val response = api.crearMascota(mascota)

                if (response.isSuccessful) {
                    Log.d("PRUEBA_XANO", "¡ÉXITO! Mascota '${mascota.nombre}' subida a Xano (ID Nube: ${response.body()?.id})")

                    // Si Xano responde con la mascota creada (y su ID nuevo), guardamos ESA versión
                    response.body()?.let { mascotaDao.insertarMascota(it) }
                } else {
                    Log.e("PRUEBA_XANO", "Error subiendo: ${response.code()} - Guardando localmente...")
                    // Si el servidor falla, guardamos la versión local
                    mascotaDao.insertarMascota(mascota)
                }
            } catch (e: Exception) {
                Log.e("PRUEBA_XANO", "Sin internet (${e.message}) - Guardando localmente...")
                // Si no hay conexión, guardamos la versión local (modo Offline)
                mascotaDao.insertarMascota(mascota)
            }
        }
    }

    // --- 4. INSERTAR SOLICITUD (Esto es lo que te faltaba) ---
    suspend fun insertarSolicitud(solicitud: Solicitud) {
        // Las solicitudes se guardan solo local por ahora (según requerimiento V2)
        solicitudDao.insertarSolicitud(solicitud)
    }

    // --- FUNCIONES PRIVADAS (Auxiliares) ---

    // Descarga de Xano y actualiza Room
    private suspend fun refreshMascotas() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getMascotas()
                if (response.isSuccessful && response.body() != null) {
                    Log.d("PRUEBA_XANO", "¡ÉXITO! Se descargaron ${response.body()!!.size} mascotas de la nube.")

                    // Insertamos cada mascota que llegó de la nube en la base de datos local
                    response.body()!!.forEach { mascota ->
                        mascotaDao.insertarMascota(mascota)
                    }
                } else {
                    Log.e("PRUEBA_XANO", "Error en descarga: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PRUEBA_XANO", "Fallo total descarga: ${e.message}")
            }
        }
    }
}