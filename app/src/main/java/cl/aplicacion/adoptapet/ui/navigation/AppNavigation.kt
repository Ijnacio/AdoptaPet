package cl.aplicacion.adoptapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.aplicacion.adoptapet.ui.screens.FeedScreen
import cl.aplicacion.adoptapet.ui.screens.AgregarMascotaScreen
import cl.aplicacion.adoptapet.ui.screens.DetalleMascotaScreen

@Composable
fun AppNavigation() {
    // 1. Crea el Controlador de Navegación
    // 'rememberNavController' es el cerebro que "recuerda" en qué pantalla estamos y maneja el historial (para poder ir "atrás").
    val navController = rememberNavController()
    // 2. Crea el "Host" de Navegación
    // 'NavHost' es el contenedor visual que mostrará la pantalla correcta  según la ruta activa.
    NavHost(
        navController = navController,  // Le pasas el cerebro
        startDestination = "feed"      //  ruta al abrir la app
    ) {
        // --- DEFINE TUS 3 RUTAS V1 ---
        // Ruta 1: El Feed 'composable' define una "parada" o pantalla en tu mapa
        composable(route = "feed") {
            FeedScreen(/* navController = navController */)
        }

        // Ruta 2: Agregar Mascota
        composable(route = "agregar") {
            AgregarMascotaScreen(/* navController = navController */)
        }

        // Ruta 3: Detalle de Mascota (con parámetro)
        composable(route = "detalle/{mascotaId}") {
            DetalleMascotaScreen(/* navController = navController */)
        }
    }
}