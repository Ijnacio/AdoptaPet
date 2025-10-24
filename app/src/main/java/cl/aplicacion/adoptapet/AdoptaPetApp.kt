package cl.aplicacion.adoptapet

import android.app.Application
import cl.aplicacion.adoptapet.model.AppDatabase
import cl.aplicacion.adoptapet.repository.MascotaRepository

/**
 * Esta clase (Application) es el "corazón" de la app.
 * Se crea ANTES que cualquier pantalla y vive siempre.
 * Es el lugar perfecto para crear nuestra Base de Datos y Repositorio.
 */
class AdoptaPetApp : Application() {

    //  Creamos la Base de Datos, lazy para que se cree solo cuando se necesite
    private val database by lazy { AppDatabase.getDatabase(this) }

    // 2. Creamos el Repositorio para la BD, el repo sera el intermediario entre la BD y los ViewModels
    val repository by lazy {
        MascotaRepository(database.mascotaDao(), database.solicitudDao())
    }
}