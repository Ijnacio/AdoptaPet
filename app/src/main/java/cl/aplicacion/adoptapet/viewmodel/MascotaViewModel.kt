package cl.aplicacion.adoptapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.repository.MascotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class MascotaViewModel(private val repository: MascotaRepository) : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())

    val mascotas: StateFlow<List<Mascota>> = _mascotas.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMascotas()
                .catch { e ->
                    // Manejar error si la DB falla
                }
                .collect { listaDeMascotas ->
                    _mascotas.value = listaDeMascotas
                }
        }
    }

    // (Podríamos añadir lógica para getMascotaById si Ignacio la necesita)
}