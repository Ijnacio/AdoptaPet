package cl.aplicacion.adoptapet.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mascota")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Datos de la mascota
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val descripcion: String,
    val vacunasAlDia: Boolean,
    val motivoAdopcion: String,

    // Datos del contacto
    val nombreContacto: String,
    val telefonoContacto: String,

    // Foto URI (puede ser URL de internet o URI local)
    val fotoUri: String
)