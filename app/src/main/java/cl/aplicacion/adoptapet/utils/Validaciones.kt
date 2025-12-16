package cl.aplicacion.adoptapet.utils

object Validaciones {

    // Función simple para probar
    fun esCorreoValido(correo: String): Boolean {
        if (correo.isBlank()) return false
        // Regex estándar para email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(correo)
    }

    fun esPasswordSegura(pass: String): Boolean {
        // La contraseña debe tener al menos 6 caracteres
        return pass.length >= 6
    }

    fun esMontoSueldoValido(monto: String): Boolean {
        // Debe ser número y mayor a 0
        return monto.all { it.isDigit() } && (monto.toIntOrNull() ?: 0) > 0
    }

    fun esNombreValido(nombre: String): Boolean {
        // No debe estar vacío y debe tener al menos 2 letras
        return nombre.isNotBlank() && nombre.length >= 2
    }
}