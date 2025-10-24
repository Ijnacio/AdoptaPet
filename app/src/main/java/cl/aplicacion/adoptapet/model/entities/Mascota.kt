package cl.aplicacion.adoptapet.model.entities
// (Asegúrate de importar 'androidx.room.Entity' y 'androidx.room.PrimaryKey')
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascota")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Datos de la mascota
    val nombre: String,
    val raza: String,
    val descripcion: String,
    val tipo: String,
    val edad: Int,
    val vacunasAlDia: Boolean,
    val motivoAdopcion: String,

    // Datos del contacto
    val nombreContacto: String,
    val telefonoContacto: String,

    // Foto URI
    val fotoUri: String,

    )