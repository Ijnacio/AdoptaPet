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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.model.entities.Solicitud
import cl.aplicacion.adoptapet.utils.SessionManager
import cl.aplicacion.adoptapet.viewmodel.FormularioViewModel
import cl.aplicacion.adoptapet.viewmodel.MascotaViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMascotaScreen(
    navController: NavHostController,
    mascotaViewModel: MascotaViewModel,
    formularioViewModel: FormularioViewModel,
    mascotaId: Int
) {
    LaunchedEffect(mascotaId) {
        mascotaViewModel.cargarMascotaPorId(mascotaId)
    }
    val mascota by mascotaViewModel.mascotaSeleccionada.collectAsState()
    DisposableEffect(Unit) {
        onDispose { mascotaViewModel.limpiarSeleccion() }
    }

    var nombreAdoptante by remember { mutableStateOf("") }
    var direccionAdoptante by remember { mutableStateOf("") }
    var motivoInteres by remember { mutableStateOf("") }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val pet = mascota!!
            val soyElDueno = pet.creador == SessionManager.correoUsuario ||
                    pet.creador == SessionManager.nombreUsuario

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Box {
                    AsyncImage(
                        model = pet.fotoUri,
                        contentDescription = "Foto de ${pet.nombre}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
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

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Sobre ${pet.nombre}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(pet.descripcion, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Motivo de Adopción:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(pet.motivoAdopcion, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Contacto Actual",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
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
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "¡Mira a ${pet.nombre}! Es un ${pet.tipo} en AdoptaPet. 🐾"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    sendIntent,
                                    "Compartir Mascota"
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Compartir", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    if (soyElDueno) {
                        Text(
                            "Administrar Publicación",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Como dueño, puedes finalizar la adopción aquí.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                mascotaViewModel.marcarComoAdoptada(pet.id)
                                Toast.makeText(
                                    context,
                                    "¡Felicidades! Adopción registrada 🐾",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "✅ ¡Ya fue Adoptada! (Finalizar)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                    } else {
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
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = direccionAdoptante,
                            onValueChange = { direccionAdoptante = it },
                            label = { Text("Tu Dirección*") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = motivoInteres,
                            onValueChange = { motivoInteres = it },
                            label = { Text("¿Por qué quieres adoptarlo?*") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Tipo de Vivienda*:", fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth()) {
                            listOf("Casa", "Depto").forEach { tipo ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { tipoVivienda = tipo }) {
                                    RadioButton(
                                        selected = tipoVivienda == tipo,
                                        onClick = { tipoVivienda = tipo })
                                    Text(tipo)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Rango de Sueldo*:", fontWeight = FontWeight.Bold)
                        Column {
                            opcionesSueldo.forEach { opcion ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { rangoSueldo = opcion }) {
                                    RadioButton(
                                        selected = rangoSueldo == opcion,
                                        onClick = { rangoSueldo = opcion })
                                    Text(opcion)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (nombreAdoptante.isBlank() || direccionAdoptante.isBlank() || motivoInteres.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "Completa todos los campos obligatorios",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val nuevaSolicitud = Solicitud(
                                        idMascota = pet.id,
                                        nombreCompleto = nombreAdoptante,
                                        direccion = direccionAdoptante,
                                        tipoVivienda = tipoVivienda,
                                        rangoSueldo = rangoSueldo,
                                        motivo = motivoInteres
                                    )
                                    scope.launch {
                                        formularioViewModel.enviarSolicitud(
                                            nuevaSolicitud
                                        )
                                    }

                                    try {
                                        val numeroDueno =
                                            pet.telefonoContacto.filter { it.isDigit() }
                                        val mensaje = """
                                            Hola! 👋 Quiero adoptar a *${pet.nombre}*.
                                            
                                            *Mis Datos:*
                                            - Nombre: $nombreAdoptante
                                            - Dirección: $direccionAdoptante
                                            - Vivienda: $tipoVivienda
                                            - Sueldo Aprox: $rangoSueldo
                                            
                                            *Motivo:* $motivoInteres
                                            
                                            ¿Podemos conversar? 🐾
                                        """.trimIndent()

                                        val mensajeCodificado = URLEncoder.encode(
                                            mensaje,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        val mensajeFinal = mensajeCodificado.replace("+", "%20")

                                        val url =
                                            "https://api.whatsapp.com/send?phone=569$numeroDueno&text=$mensajeFinal"

                                        context.startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                android.net.Uri.parse(url)
                                            )
                                        )
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "No se pudo abrir WhatsApp, guardado local.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    Toast.makeText(
                                        context,
                                        "¡Solicitud generada!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = nombreAdoptante.isNotBlank() && direccionAdoptante.isNotBlank() && motivoInteres.isNotBlank()
                        ) {
                            Text("Enviar Solicitud y Contactar", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}