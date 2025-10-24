package cl.aplicacion.adoptapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.repository.MascotaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MascotaViewModel(private val repository: MascotaRepository) : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas.asStateFlow()

    // --- PARA LA PANTALLA DE DETALLE ---
    private val _mascotaIdSeleccionada = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val mascotaSeleccionada: StateFlow<Mascota?> = _mascotaIdSeleccionada.flatMapLatest { id ->
        if (id == null) {
            flowOf(null)
        } else {
            repository.getMascotaById(id)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun cargarMascotaPorId(id: Int) {
        _mascotaIdSeleccionada.value = id
    }

    fun limpiarSeleccion() {
        _mascotaIdSeleccionada.value = null
    }

    // --- INICIALIZACIÓN ---
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
}