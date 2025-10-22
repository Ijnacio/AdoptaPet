package cl.aplicacion.adoptapet.model.dao

import androidx.room.Dao
import androidx.room.Insert
import cl.aplicacion.adoptapet.model.entities.Solicitud

@Dao
interface SolicitudDao {

    // Función 'suspend' para guardar la solicitud del formulario
    @Insert
    suspend fun insertarSolicitud(solicitud: Solicitud)
}