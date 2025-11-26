package cl.aplicacion.adoptapet.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import cl.aplicacion.adoptapet.utils.SessionManager

@Composable
fun FeedScreen(
    navController: NavHostController,
    viewModel: MascotaViewModel
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var mostrarSoloMisMascotas by remember { mutableStateOf(false) }
    var filtroTipo by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Perro", "Gato", "Conejo", "Pájaro", "Otro")

    val mascotasFiltradas = mascotas.filter { mascota ->
        val pasaFiltroUsuario = if (mostrarSoloMisMascotas) {
            mascota.creador == SessionManager.nombreUsuario || mascota.creador == SessionManager.correoUsuario
        } else {
            true
        }

        val pasaFiltroTipo = if (filtroTipo == "Todos") {
            true
        } else {
            mascota.tipo.equals(filtroTipo, ignoreCase = true)
        }

        pasaFiltroUsuario && pasaFiltroTipo
    }

    val atBottom by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = info.totalItemsCount
            total > 0 && lastVisible >= total - 1
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "🐾 AdoptaPet",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        if (SessionManager.isLoggedIn()) {
                            IconButton(
                                onClick = {
                                    SessionManager.logout()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Cerrar Sesión",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (SessionManager.isLoggedIn()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FilterChip(
                                selected = mostrarSoloMisMascotas,
                                onClick = { mostrarSoloMisMascotas = !mostrarSoloMisMascotas },
                                label = { Text("Mis Publicaciones") },
                                leadingIcon = if (mostrarSoloMisMascotas) {
                                    { Icon(Icons.Default.Person, contentDescription = null) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categorias.forEach { categoria ->
                            FilterChip(
                                selected = filtroTipo == categoria,
                                onClick = { filtroTipo = categoria },
                                label = { Text(categoria) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                AnimatedVisibility(
                    visible = atBottom,
                    enter = fadeIn() + expandIn(),
                    exit = fadeOut() + shrinkOut()
                ) {
                    FloatingActionButton(
                        onClick = { scope.launch { listState.animateScrollToItem(0) } },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Subir")
                    }
                }

                FloatingActionButton(
                    onClick = { navController.navigate("agregar") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar Mascota")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        if (mascotasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "😿",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val textoMensaje = if (mostrarSoloMisMascotas)
                        "No has publicado mascotas aún"
                    else if (filtroTipo != "Todos")
                        "No hay mascotas de tipo $filtroTipo"
                    else
                        "Aún no hay mascotas en adopción"

                    Text(
                        text = textoMensaje,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(mascotasFiltradas, key = { it.id }) { mascota ->
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

@Composable
fun PetCard(mascota: Mascota, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column {
            Box {
                AsyncImage(
                    model = mascota.fotoUri,
                    contentDescription = "Foto de ${mascota.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = mascota.tipo,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mascota.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "${mascota.edad} años",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Raza: ${mascota.raza}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                mascota.creador?.let { creador ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Publicado por: $creador",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = mascota.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (mascota.vacunasAlDia) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (mascota.vacunasAlDia) Color(0xFF4CAF50) else Color(0xFFF44336),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (mascota.vacunasAlDia) "Vacunas al día" else "Vacunas pendientes",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (mascota.vacunasAlDia) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Contacto:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = mascota.nombreContacto,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Button(
                        onClick = onCardClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Ver más", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}