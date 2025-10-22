package cl.aplicacion.adoptapet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel
import coil.compose.AsyncImage // <-- ¡El import de Coil!

@Composable
fun FeedScreen(
    navController: NavHostController,
    viewModel: MascotaViewModel
) {
    val mascotas by viewModel.mascotas.collectAsState()

    // 1. Usamos 'Scaffold' para añadir el Botón Flotante (FAB)
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // 2. ¡Navegación real! Ya no necesitas el truco de 'startDestination'
                    navController.navigate("agregar")
                }
            ) {
                Icon(Icons.Filled.Add, "Agregar Mascota")
            }
        }
    ) { paddingValues -> // paddingValues es el espacio que ocupa el botón

        if (mascotas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay mascotas en adopción...")
            }
        } else {
            // 3. La lista (LazyColumn)
            LazyColumn(
                modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)
            ) {
                items(mascotas) { mascota ->
                    // 4. ¡La Card "Tipo Instagram"!
                    PetCard(
                        mascota = mascota,
                        onCardClick = {
                            navController.navigate("detalle/${mascota.id}")
                        }
                    )
                }
            }
        }
    }
}

// 5. El Composable de la Card (para un código más limpio)
@Composable
fun PetCard(mascota: cl.aplicacion.adoptapet.model.entities.Mascota, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // 6. La FOTO (¡AsyncImage carga la URL de internet!)
            AsyncImage(
                model = mascota.fotoUri, // La URL que Francisca puso en la DB
                contentDescription = "Foto de ${mascota.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // Proporción 1:1 (cuadrada)
                contentScale = ContentScale.Crop // Rellena el espacio
            )

            // 7. El TEXTO
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = mascota.nombre,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "Raza: ${mascota.raza}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}