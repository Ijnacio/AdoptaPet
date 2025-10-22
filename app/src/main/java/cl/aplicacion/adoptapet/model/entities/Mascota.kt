package cl.aplicacion.adoptapet.model.entities
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mascota")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val fotoUri: String, //
    val raza: String,
    val edad: Int,
    val vacunasAlDia: Boolean,
    val motivoAdopcion: String,
    val descripcion: String,
    val nombreContacto: String,
    val telefonoContacto: String
)