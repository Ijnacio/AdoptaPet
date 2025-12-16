package cl.aplicacion.adoptapet.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "mascota")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val descripcion: String,

    @SerializedName("vacunas_al_dia")
    val vacunasAlDia: Boolean,

    @SerializedName("motivo_adopcion")
    val motivoAdopcion: String,

    @SerializedName("nombre_contacto")
    val nombreContacto: String,

    @SerializedName("telefono_contacto")
    val telefonoContacto: String,

    @SerializedName("foto_uri")
    val fotoUri: String,

    val creador: String? = null
)