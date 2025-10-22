package cl.aplicacion.adoptapet.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.aplicacion.adoptapet.model.entities.Mascota
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {

    @Insert
    suspend fun insertarMascota(mascota: Mascota)

    // @Query: Aquí escribimos la consulta SQL
    // Esta función obtiene TODAS las mascotas y las ordena (DESC = descendente)
    @Query("SELECT * FROM mascota ORDER BY id DESC")
    fun getAllMascotas(): Flow<List<Mascota>>

    // Esta función obtiene UNA mascota específica por su ID
    @Query("SELECT * FROM mascota WHERE id = :idMascota")
    fun getMascotaById(idMascota: Int): Flow<Mascota>
}