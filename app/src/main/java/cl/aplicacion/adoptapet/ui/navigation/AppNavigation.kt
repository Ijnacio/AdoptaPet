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

@Composable
fun AppNavigation() {

    //controlador principal de navegación
    val navController = rememberNavController()

    //obtenemos la app y la factory para los ViewModels

    val application = LocalContext.current.applicationContext as AdoptaPetApp
    val factory = AppViewModelFactory(application.repository)

    // VIEWMODELS: se comparten entre pantallas dentro de este NavHost

    val mascotaViewModel: MascotaViewModel = viewModel(factory = factory)
    val formularioViewModel: FormularioViewModel = viewModel(factory = factory)


    // define destinos (routes) y pantallas
    NavHost(navController = navController, startDestination = "feed") {

        // se conecta al feed de mascotas
        composable(route = "feed") {
            FeedScreen(
                navController = navController,
                viewModel = mascotaViewModel
            )
        }

        // agregar mascota
        composable(route = "agregar") {
            AgregarMascotaScreen(
                navController = navController,
                viewModel = formularioViewModel
            )
        }

        //  detalle mascota y id como parámetro
        composable(route = "detalle/{mascotaId}") { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("mascotaId")
            val id = idString?.toIntOrNull() ?: 0

            // pantalla de detalle y paso de viewmodels y id
            DetalleMascotaScreen(
                navController = navController,
                mascotaViewModel = mascotaViewModel,
                formularioViewModel = formularioViewModel,
                mascotaId = id
            )
        }
    }
}
