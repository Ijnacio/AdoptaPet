package cl.aplicacion.adoptapet.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitud_adopcion")
data class Solicitud(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val idMascota: Int, // ID de la mascota
    val nombreCompleto: String,
    val direccion: String,
    val tipoVivienda: String, // "Casa" o "Depto"
    val rangoSueldo: String // "Menos de 500mil", "500mil - 1 millón", "Más de 1 millón"
)