package cl.aplicacion.adoptapet.model.entities
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "solicitud_adopcion")
data class Solicitud(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val idMascota: Int, // Esta es la "llave foránea" que nos dice para qué mascota es
    val nombreCompleto: String,
    val direccion: String,
    val tipoVivienda: String, // "Casa" o "Depto"
    val aceptaEsterilizacion: Boolean
)