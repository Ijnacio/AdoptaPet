package cl.aplicacion.adoptapet.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitud_adopcion")
data class Solicitud(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val idMascota: Int,
    val nombreCompleto: String,
    val direccion: String,
    val tipoVivienda: String,
    val rangoSueldo: String,
    val motivo: String
)