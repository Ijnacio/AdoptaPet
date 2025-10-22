package cl.aplicacion.adoptapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.model.entities.Solicitud
import cl.aplicacion.adoptapet.repository.MascotaRepository
import kotlinx.coroutines.launch

class FormularioViewModel(private val repository: MascotaRepository) : ViewModel() {


    fun agregarMascota(mascota: Mascota) {
        viewModelScope.launch {
            repository.insertarMascota(mascota)
        }
    }

    fun enviarSolicitud(solicitud: Solicitud) {
        viewModelScope.launch {
            repository.insertarSolicitud(solicitud)
        }
    }
}