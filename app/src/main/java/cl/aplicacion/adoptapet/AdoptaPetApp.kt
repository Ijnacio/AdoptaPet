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

    // 1. Creamos la Base de Datos (usando el código de Francisca)
    // 'lazy' significa que solo se creará la primera vez que se necesite.
    private val database by lazy { AppDatabase.getDatabase(this) }

    // 2. Creamos el Repositorio (y le pasamos los DAOs de la DB)
    //    Este 'repository' será público para que los ViewModels lo usen.
    val repository by lazy {
        MascotaRepository(database.mascotaDao(), database.solicitudDao())
    }
}