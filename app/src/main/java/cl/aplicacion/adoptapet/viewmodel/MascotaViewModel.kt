package cl.aplicacion.adoptapet.viewmodel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.repository.MascotaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


class MascotaViewModel(private val repository: MascotaRepository) : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas.asStateFlow()

    // --- CAMBIOS PARA LA PANTALLA DE DETALLE ---

    // 1. Un StateFlow PRIVADO que guarda el ID de la mascota que estamos viendo.
    //    Inicia como null (ninguna seleccionada).
    private val _mascotaIdSeleccionada = MutableStateFlow<Int?>(null)

    // 2. Un StateFlow PÚBLICO para la mascota seleccionada.
    //    Usamos 'flatMapLatest':
    //    - "Escucha" a _mascotaIdSeleccionada.
    //    - Cuando el ID cambia, cancela la búsqueda anterior (si había una).
    //    - Inicia una NUEVA búsqueda en el repositorio con el nuevo ID.
    //    - Si el ID es null, simplemente emite 'null' (gracias a flowOf(null)).
    @OptIn(ExperimentalCoroutinesApi::class) // Requerido para flatMapLatest
    val mascotaSeleccionada: StateFlow<Mascota?> = _mascotaIdSeleccionada.flatMapLatest { id ->
        if (id == null) {
            flowOf(null) // Emite null si no hay ID
        } else {
            repository.getMascotaById(id) // Busca en el repo si SÍ hay ID
        }
    }.stateIn( // Convertimos el Flow resultante en un StateFlow "caliente"
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // 3. Una función PÚBLICA que la UI llamará para "decirle" al VM qué mascota cargar.
    fun cargarMascotaPorId(id: Int) {
        // Simplemente actualizamos el ID. flatMapLatest hará el resto.
        _mascotaIdSeleccionada.value = id
    }

    // 4. (Opcional) Una función para "limpiar" la selección cuando el usuario
    //    salga de la pantalla de detalle, si es necesario.
    fun limpiarSeleccion() {
        _mascotaIdSeleccionada.value = null
    }

    // --- FIN DE LOS CAMBIOS ---


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

    // Esta función ya no es necesaria con el nuevo patrón.
    /*
    fun getMascotaById(id: Int): StateFlow<Mascota?> {
        return repository.getMascotaById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }
    */
}
