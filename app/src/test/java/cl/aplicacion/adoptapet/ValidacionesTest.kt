package cl.aplicacion.adoptapet

import cl.aplicacion.adoptapet.utils.Validaciones
import org.junit.Test
import org.junit.Assert.*

class ValidacionesTest {

    // --- PRUEBAS DE CORREO ---
    @Test
    fun correo_formatoCorrecto_retornaTrue() {
        assertTrue(Validaciones.esCorreoValido("profe@duoc.cl"))
    }

    @Test
    fun correo_sinArroba_retornaFalse() {
        assertFalse(Validaciones.esCorreoValido("profeduoc.cl"))
    }

    @Test
    fun correo_vacio_retornaFalse() {
        assertFalse(Validaciones.esCorreoValido(""))
    }

    // --- PRUEBAS DE CONTRASEÑA ---
    @Test
    fun password_muyCorta_retornaFalse() {
        assertFalse(Validaciones.esPasswordSegura("12345"))
    }

    @Test
    fun password_segura_retornaTrue() {
        assertTrue(Validaciones.esPasswordSegura("123456"))
    }

    // --- PRUEBAS DE SUELDO ---
    @Test
    fun sueldo_numeros_retornaTrue() {
        assertTrue(Validaciones.esMontoSueldoValido("500000"))
    }

    @Test
    fun sueldo_texto_retornaFalse() {
        assertFalse(Validaciones.esMontoSueldoValido("Mucho Dinero"))
    }
}