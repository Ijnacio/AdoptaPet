package cl.aplicacion.adoptapet.utils

object SessionManager {
    // Datos del usuario en sesión
    var nombreUsuario: String? = null
    var correoUsuario: String? = null

    // Estado de conexión
    fun isLoggedIn(): Boolean {
        return !nombreUsuario.isNullOrBlank()
    }

    // "Login": Guardamos los datos en memoria
    fun login(nombre: String, correo: String) {
        nombreUsuario = nombre
        correoUsuario = correo
    }

    fun logout() {
        nombreUsuario = null
        correoUsuario = null
    }
}