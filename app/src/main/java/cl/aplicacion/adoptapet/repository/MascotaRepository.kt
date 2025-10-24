package cl.aplicacion.adoptapet.repository


import cl.aplicacion.adoptapet.model.dao.MascotaDao
import cl.aplicacion.adoptapet.model.dao.SolicitudDao
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.model.entities.Solicitud
import kotlinx.coroutines.flow.Flow


class MascotaRepository(
    private val mascotaDao: MascotaDao,
    private val solicitudDao: SolicitudDao
) {

    fun getAllMascotas(): Flow<List<Mascota>> {
        return mascotaDao.getAllMascotas()
    }

    fun getMascotaById(id: Int): Flow<Mascota> {
        return mascotaDao.getMascotaById(id)
    }

    // Esta función llama a la función 'suspend' del DAO
    suspend fun insertarMascota(mascota: Mascota) {
        mascotaDao.insertarMascota(mascota)
    }

    suspend fun insertarSolicitud(solicitud: Solicitud) {
        solicitudDao.insertarSolicitud(solicitud)
    }
}