package cl.aplicacion.adoptapet.utils

object SessionManager {
    // Aquí guardaremos el nombre del usuario temporalmente
    var usuarioActual: String? = null

    fun isLogged(): Boolean {
        return !usuarioActual.isNullOrBlank()
    }

    fun logout() {
        usuarioActual = null
    }
}