package cl.aplicacion.adoptapet

import android.app.Application
import cl.aplicacion.adoptapet.model.AppDatabase
import cl.aplicacion.adoptapet.repository.MascotaRepository
import cl.aplicacion.adoptapet.utils.SessionManager // <-- Importar

class AdoptaPetApp : Application() {

    // Base de datos
    private val database by lazy { AppDatabase.getDatabase(this) }

    // Repositorio
    val repository by lazy {
        MascotaRepository(database.mascotaDao(), database.solicitudDao())
    }

    // --- ESTO ES LO NUEVO: Inicializar SessionManager ---
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}