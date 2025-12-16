package cl.aplicacion.adoptapet.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "AdoptaPetSession"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_CORREO = "correo"
    private const val KEY_CONTADOR = "contador_adopciones"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun login(nombre: String, correo: String) {
        preferences.edit().apply {
            putString(KEY_NOMBRE, nombre)
            putString(KEY_CORREO, correo)
            apply()
        }
    }

    fun logout() {
        preferences.edit().apply {
            remove(KEY_NOMBRE)
            remove(KEY_CORREO)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return !preferences.getString(KEY_CORREO, null).isNullOrBlank()
    }

    val nombreUsuario: String?
        get() = preferences.getString(KEY_NOMBRE, null)

    val correoUsuario: String?
        get() = preferences.getString(KEY_CORREO, null)

    // --- LÓGICA DEL CONTADOR DE ADOPCIONES ---
    fun incrementarAdopciones() {
        val actual = getCantidadAdopciones()
        preferences.edit().putInt(KEY_CONTADOR, actual + 1).apply()
    }

    fun getCantidadAdopciones(): Int {
        // Empezamos en 12 como base para que se vea bonito
        return preferences.getInt(KEY_CONTADOR, 12)
    }
}