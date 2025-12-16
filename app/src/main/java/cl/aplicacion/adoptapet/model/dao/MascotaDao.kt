package cl.aplicacion.adoptapet.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.aplicacion.adoptapet.model.entities.Mascota
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMascota(mascota: Mascota)
    @Query("DELETE FROM mascota")
    suspend fun borrarTodo()

    // CONSULTAS
    @Query("SELECT * FROM mascota ORDER BY id DESC")
    fun getAllMascotas(): Flow<List<Mascota>>

    @Query("SELECT * FROM mascota WHERE id = :idMascota")
    fun getMascotaById(idMascota: Int): Flow<Mascota>

    // ... dentro de la interface MascotaDao
    @Query("DELETE FROM mascota WHERE id = :id")
    suspend fun eliminarMascota(id: Int)

}