package cl.aplicacion.adoptapet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.aplicacion.adoptapet.AdoptaPetApp
import cl.aplicacion.adoptapet.ui.screens.AgregarMascotaScreen
import cl.aplicacion.adoptapet.ui.screens.DetalleMascotaScreen
import cl.aplicacion.adoptapet.ui.screens.FeedScreen
import cl.aplicacion.adoptapet.viewmodel.AppViewModelFactory
import cl.aplicacion.adoptapet.viewmodel.FormularioViewModel
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel
import cl.aplicacion.adoptapet.ui.screens.LoginScreen
import cl.aplicacion.adoptapet.utils.SessionManager // <--- IMPORTANTE: Importar esto

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val application = LocalContext.current.applicationContext as AdoptaPetApp
    val factory = AppViewModelFactory(application.repository)

    val mascotaViewModel: MascotaViewModel = viewModel(factory = factory)
    val formularioViewModel: FormularioViewModel = viewModel(factory = factory)

    val destinoInicial = if (SessionManager.isLoggedIn()) "feed" else "login"



    NavHost(navController = navController, startDestination = destinoInicial) {

        // PANTALLA 1: LOGIN
        composable(route = "login") {
            LoginScreen(navController = navController)
        }

        // PANTALLA 2: FEED (Lista de mascotas)
        composable(route = "feed") {
            FeedScreen(
                navController = navController,
                viewModel = mascotaViewModel
            )
        }

        composable(route = "agregar") {
            AgregarMascotaScreen(
                navController = navController,
                viewModel = formularioViewModel
            )
        }

        // PANTALLA 4: DETALLE MASCOTA (Recibe ID como parámetro)
        composable(route = "detalle/{mascotaId}") { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("mascotaId")
            val id = idString?.toIntOrNull() ?: 0

            // Pantalla de detalle y paso de viewmodels y id
            DetalleMascotaScreen(
                navController = navController,
                mascotaViewModel = mascotaViewModel,
                formularioViewModel = formularioViewModel,
                mascotaId = id
            )
        }
    }
}