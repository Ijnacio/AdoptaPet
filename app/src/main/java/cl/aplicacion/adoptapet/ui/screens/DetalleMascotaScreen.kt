package cl.aplicacion.adoptapet.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.model.entities.Solicitud
import cl.aplicacion.adoptapet.viewmodel.FormularioViewModel
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMascotaScreen(
    navController: NavHostController,
    mascotaViewModel: MascotaViewModel,
    formularioViewModel: FormularioViewModel,
    mascotaId: Int
) {
    // --- CARGAR MASCOTA ---
    LaunchedEffect(mascotaId) {
        mascotaViewModel.cargarMascotaPorId(mascotaId)
    }
    val mascota by mascotaViewModel.mascotaSeleccionada.collectAsState()
    DisposableEffect(Unit) {
        onDispose { mascotaViewModel.limpiarSeleccion() }
    }

    // --- ESTADO DEL FORMULARIO ---
    var nombreAdoptante by remember { mutableStateOf("") }
    var direccionAdoptante by remember { mutableStateOf("") }
    var tipoVivienda by remember { mutableStateOf("Casa") }
    var rangoSueldo by remember { mutableStateOf("Menos de 400mil") }
    val opcionesSueldo = listOf(
        "Menos de 200mil",
        "200mil - 400mil",
        "400mil - 600mil",
        "600mil - 800mil",
        "800mil - 1 millón",
    )



    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        if (mascota == null) {
            // Estado de carga
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando mascota...", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            val pet = mascota!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- FOTO PRINCIPAL ---
                Box {
                    AsyncImage(
                        model = pet.fotoUri,
                        contentDescription = "Foto de ${pet.nombre}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Badge del tipo (esquina superior derecha)
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = pet.tipo,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- INFORMACIÓN PRINCIPAL ---
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Nombre y Edad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pet.nombre,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "${pet.edad} años",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = pet.raza,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado de vacunas
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (pet.vacunasAlDia) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (pet.vacunasAlDia) Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (pet.vacunasAlDia) "Vacunas al día" else "Vacunas pendientes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (pet.vacunasAlDia) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Card con descripción y motivo
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Sobre ${pet.nombre}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                pet.descripcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Motivo de Adopción:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                pet.motivoAdopcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card con info de contacto
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Contacto Actual",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                pet.nombreContacto,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "📞 ${pet.telefonoContacto}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }


                    // --- BOTÓN WHATSAPP (NUEVO) ---
                    Button(
                        onClick = {
                            try {
                                // Limpiamos el número para dejar solo dígitos
                                val numeroLimpio = pet.telefonoContacto.filter { it.isDigit() }

                                // Mensaje predefinido
                                val mensaje = "Hola! Vi a ${pet.nombre} en AdoptaPet y me interesa adoptarlo/a. 🐾"

                                // Intent para abrir WhatsApp
                                val url = "https://api.whatsapp.com/send?phone=569$numeroLimpio&text=$mensaje"
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF25D366), // Verde WhatsApp
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        // Puedes usar un ícono si tienes, si no texto está bien
                        Text("Contactar Dueño por WhatsApp 💬", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "¡Mira a ${pet.nombre}! Es un ${pet.tipo.lowercase()} ${pet.raza} de ${pet.edad} años en adopción. 🐾 Más info en AdoptaPet."
                                )
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Compartir Mascota")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Compartir con un Amigo", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(24.dp))

                    // --- FORMULARIO DE ADOPCIÓN ---
                    Text(
                        "¿Quieres Adoptar a ${pet.nombre}?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nombreAdoptante,
                        onValueChange = { nombreAdoptante = it },
                        label = { Text("Tu Nombre Completo*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = direccionAdoptante,
                        onValueChange = { direccionAdoptante = it },
                        label = { Text("Tu Dirección*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tipo de Vivienda
                    Text(
                        "Tipo de Vivienda*:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { tipoVivienda = "Casa" }
                        ) {
                            RadioButton(
                                selected = tipoVivienda == "Casa",
                                onClick = { tipoVivienda = "Casa" }
                            )
                            Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Casa")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { tipoVivienda = "Depto" }
                        ) {
                            RadioButton(
                                selected = tipoVivienda == "Depto",
                                onClick = { tipoVivienda = "Depto" }
                            )
                            Text("Depto")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rango de Sueldo
                    Text(
                        "Rango de Sueldo Aproximado*:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(Modifier.fillMaxWidth()) {
                        opcionesSueldo.forEach { opcion ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { rangoSueldo = opcion }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = rangoSueldo == opcion,
                                    onClick = { rangoSueldo = opcion }
                                )
                                Text(opcion, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- BOTONES ---
                    Button(
                        onClick = {
                            if (nombreAdoptante.isBlank() || direccionAdoptante.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Por favor completa todos los campos obligatorios",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val nuevaSolicitud = Solicitud(
                                    idMascota = pet.id,
                                    nombreCompleto = nombreAdoptante,
                                    direccion = direccionAdoptante,
                                    tipoVivienda = tipoVivienda,
                                    rangoSueldo = rangoSueldo
                                )
                                scope.launch {
                                    formularioViewModel.enviarSolicitud(nuevaSolicitud)
                                }
                                Toast.makeText(
                                    context,
                                    "¡Solicitud enviada! Te contactaremos pronto 🐾",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = nombreAdoptante.isNotBlank() && direccionAdoptante.isNotBlank()
                    ) {
                        Text(
                            "Enviar Solicitud de Adopción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                }
            }
        }
    }
}