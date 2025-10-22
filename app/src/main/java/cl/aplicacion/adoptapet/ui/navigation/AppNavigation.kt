package cl.aplicacion.adoptapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel // Importa el "viewModel" de compose
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.aplicacion.adoptapet.AdoptaPetApp // <-- Importa tu App (Paso 1)
import cl.aplicacion.adoptapet.ui.screens.AgregarMascotaScreen
import cl.aplicacion.adoptapet.ui.screens.DetalleMascotaScreen
import cl.aplicacion.adoptapet.ui.screens.FeedScreen
import cl.aplicacion.adoptapet.viewmodel.AppViewModelFactory // <-- Importa la Fábrica (Paso 2)
import cl.aplicacion.adoptapet.viewmodel.FormularioViewModel
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // --- INYECCIÓN DE DEPENDENCIAS (EL "PEGAMENTO") ---
    val application = LocalContext.current.applicationContext as AdoptaPetApp
    val factory = AppViewModelFactory(application.repository)

    // Creamos los ViewModels USANDO la fábrica
    val mascotaViewModel: MascotaViewModel = viewModel(factory = factory)
    val formularioViewModel: FormularioViewModel = viewModel(factory = factory)

    // --- FIN DEL PEGAMENTO ---

    // 4. Creamos el NavHost (tu mapa de navegación)
    NavHost(navController = navController, startDestination = "feed") {

        composable(route = "feed") {
            // 5. ¡Le pasamos el VM y el NavController a tu pantalla!
            FeedScreen(
                navController = navController,
                viewModel = mascotaViewModel
            )
        }

        composable(route = "agregar") {
            // 6. ¡Le pasamos el VM y el NavController a tu otra pantalla!
            AgregarMascotaScreen(
                navController = navController,
                viewModel = formularioViewModel
            )
        }

        composable(route = "detalle/{mascotaId}") {
                backStackEntry -> // <-- backStackEntry tiene los argumentos
            // 1. Extraemos el "mascotaId" de la ruta (viene como texto)
            val idString = backStackEntry.arguments?.getString("mascotaId")
            // 2. Lo convertimos a número (Int). Si falla, usamos 0.
            val id = idString?.toIntOrNull() ?: 0

            // 3. ¡Le pasamos el ID extraído a tu pantalla!
            DetalleMascotaScreen(
                navController = navController,
                viewModel = mascotaViewModel,
                mascotaId = id // <-- Aquí le pasas el ID
            )
        }
        }
    }
