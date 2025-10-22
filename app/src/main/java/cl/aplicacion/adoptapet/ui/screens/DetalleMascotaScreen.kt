package cl.aplicacion.adoptapet.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.* // Import genérico para Layouts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.* // Import genérico para Material 3
import androidx.compose.runtime.* // Import genérico para Runtime (LaunchedEffect, collectAsState)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel

// Anotación necesaria para usar TopAppBar (componente experimental)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMascotaScreen(
    navController: NavHostController, // Para poder volver atrás
    viewModel: MascotaViewModel,      // Para buscar la mascota
    mascotaId: Int                    // El ID que viene de AppNavigation
) {
    // --- 1. LÓGICA PARA CARGAR LA MASCOTA CORRECTA ---

    // 'LaunchedEffect' ejecuta su bloque UNA SOLA VEZ cuando entra a la pantalla
    // (o si 'mascotaId' cambia, lo cual no pasará aquí).
    // Es el lugar CORRECTO para decirle al ViewModel "¡Carga esta mascota!".
    LaunchedEffect(key1 = mascotaId) {
        viewModel.cargarMascotaPorId(mascotaId)
    }

    // 'collectAsState(null)' observa el StateFlow 'mascotaSeleccionada' del ViewModel.
    // Empieza como 'null' (mientras carga). Cuando el ViewModel encuentra la mascota,
    // esta variable 'mascota' se actualizará y la pantalla se redibujará.
    val mascota by viewModel.mascotaSeleccionada.collectAsState()

    // (Opcional) Limpia la selección cuando salimos de esta pantalla.
    // 'DisposableEffect' ejecuta 'onDispose' cuando el Composable desaparece.
    DisposableEffect(Unit) {
        onDispose {
            viewModel.limpiarSeleccion()
        }
    }

    // --- LÓGICA PARA COMPARTIR ---
    val context = LocalContext.current // Necesario para el Intent

    // --- DISEÑO DE LA PANTALLA CON BARRA SUPERIOR ---
    Scaffold(
        topBar = {
            // Barra superior con título y flecha para volver
            TopAppBar(
                title = { Text("Detalle de Mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Vuelve atrás
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues -> // Espacio que deja la barra superior

        // --- CONTENIDO PRINCIPAL ---
        // Verificamos si la mascota ya cargó (si NO es null)
        if (mascota == null) {
            // --- ESTADO DE CARGA ---
            // Si 'mascota' es null, muestra un indicador de carga
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // Ruedita de carga
                Text(" Cargando mascota...", modifier = Modifier.padding(top = 60.dp))
            }
        } else {
            // --- ESTADO CARGADO (Mascota encontrada) ---
            // Si 'mascota' NO es null, usamos 'pet' para mostrar sus datos.
            val pet = mascota!! // !! es seguro aquí porque ya comprobamos que no es null

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplica el padding de la barra
                    .padding(16.dp), // Tu padding normal
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- MOSTRAMOS DATOS REALES ---
                Text("Nombre: ${pet.nombre}", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Raza: ${pet.raza}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Edad: ${pet.edad} años", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Descripción:", style = MaterialTheme.typography.titleMedium)
                Text(pet.descripcion, style = MaterialTheme.typography.bodyMedium)
                // (Puedes añadir más campos aquí: vacunas, motivoAdopcion, etc.)

                Spacer(modifier = Modifier.height(32.dp))

                // --- FORMULARIO DE ADOPCIÓN (PENDIENTE V1.1) ---
                Text("(Aquí irá el formulario de adopción)", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(32.dp))

                // --- BOTÓN COMPARTIR (Recurso Nativo 2) ---
                Button(onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        // ¡Usa los datos reales de 'pet'!
                        putExtra(Intent.EXTRA_TEXT, "¡Mira esta mascota para adoptar! Se llama ${pet.nombre} (${pet.raza}). Más info en AdoptaPet.")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Compartir Mascota")
                    context.startActivity(shareIntent)
                }) {
                    Text("Compartir con un Amigo")
                }
            } // Fin Column
        } // Fin else (mascota != null)
    } // Fin Scaffold
}
