package cl.aplicacion.adoptapet.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName // <--- IMPORTANTE: Agrega este import

@Entity(tableName = "mascota")
data class Mascota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Estos suelen funcionar bien si son una sola palabra
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val descripcion: String,

    // AQUÍ ESTÁ LA MAGIA: Traducimos los nombres para Xano

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

    // Agregamos 'creador' para cuando terminemos el login
    val creador: String? = null
)