package cl.aplicacion.adoptapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.aplicacion.adoptapet.repository.MascotaRepository

/**
 * Esta es la "Fábrica" de ViewModels.
 * Su única pega es recibir el Repositorio y usarlo para
 * construir el ViewModel que le pidan.
 */
class AppViewModelFactory(private val repository: MascotaRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Revisa qué ViewModel nos están pidiendo y lo crea
        if (modelClass.isAssignableFrom(MascotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MascotaViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(FormularioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormularioViewModel(repository) as T
        }
        // Si pide un ViewModel desconocido, lanza un error
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}