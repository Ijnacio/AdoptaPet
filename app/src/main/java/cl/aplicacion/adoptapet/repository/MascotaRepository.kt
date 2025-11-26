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
    private val api = RetrofitClient.api

    fun getAllMascotas(): Flow<List<Mascota>> {
        CoroutineScope(Dispatchers.IO).launch {
            refreshMascotas()
        }
        return mascotaDao.getAllMascotas()
    }

    fun getMascotaById(id: Int): Flow<Mascota> {
        return mascotaDao.getMascotaById(id)
    }

    suspend fun insertarMascota(mascota: Mascota) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.crearMascota(mascota)
                if (response.isSuccessful && response.body() != null) {
                    mascotaDao.insertarMascota(response.body()!!)
                } else {
                    mascotaDao.insertarMascota(mascota)
                }
            } catch (e: Exception) {
                Log.e("REPO", "Error subiendo: ${e.message}")
                mascotaDao.insertarMascota(mascota)
            }
        }
    }

    suspend fun insertarSolicitud(solicitud: Solicitud) {
        solicitudDao.insertarSolicitud(solicitud)
    }

    private suspend fun refreshMascotas() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getMascotas()
                if (response.isSuccessful && response.body() != null) {
                    val listaNube = response.body()!!

                    if (listaNube.isNotEmpty()) {

                        mascotaDao.borrarTodo()

                        // 2. Insertamos la lista fresca de la nube
                        listaNube.forEach { mascota ->
                            mascotaDao.insertarMascota(mascota)
                        }
                        Log.d("REPO", "Sincronización exitosa: ${listaNube.size} mascotas.")
                    }
                }
            } catch (e: Exception) {
                Log.e("REPO", "Error sincronizando: ${e.message}")
                // Si falla, no borramos nada, mostramos lo que haya en caché (Offline mode)
            }
        }
    }
}