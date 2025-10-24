package cl.aplicacion.adoptapet.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.viewmodel.FormularioViewModel
import kotlinx.coroutines.launch
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@Composable
fun AgregarMascotaScreen(
    navController: NavHostController,
    viewModel: FormularioViewModel
) {

    // --- LÓGICA DE LA CÁMARA ---
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    fun getTmpFileUri(): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(null)
        val tmpFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(Objects.requireNonNull(context),"${context.packageName}.provider", tmpFile)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success -> if (!success) photoUri = null }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val uri = getTmpFileUri()
                photoUri = uri
                takePictureLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // --- VARIABLES DE ESTADO ---
    // Campos Mascota
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var tipoMascota by remember { mutableStateOf("Perro") }
    var edad by remember { mutableStateOf("") }
    var vacunasAlDia by remember { mutableStateOf(false) }
    var motivoAdopcion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    // Lista de tipos de animales
    val tiposDeAnimales = listOf("Perro", "Gato", "Conejo", "Pájaro", "Otro")

    // Campos Contacto
    var nombreContacto by remember { mutableStateOf("") }
    var telefonoContacto by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // --- UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // TÍTULO PRINCIPAL
        Text(
            "Agregar Nueva Mascota",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: DATOS DE LA MASCOTA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Datos de la Mascota",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre*", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors( // Colores de los bordes jeje
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Raza
                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Tipo de Animal (Radio Buttons)
                Text(
                    "Tipo de Animal*:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))


                Column(Modifier.fillMaxWidth()) {
                    tiposDeAnimales.forEach { tipo ->
                        Row( // Fila para cada opción
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { tipoMascota = tipo }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = (tipoMascota == tipo),
                                onClick = { tipoMascota = tipo }
                            )
                            Text(
                                tipo,
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // edad
                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it.filter { c -> c.isDigit() } }, // Solo permite dígitos
                    label = { Text("Edad (años)", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Teclado numérico
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // vacunas al día
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Alinea  el checkbox y el texto
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vacunasAlDia = !vacunasAlDia }// Permite hacer clic en todo el row
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = vacunasAlDia,
                        onCheckedChange = { vacunasAlDia = it }
                    )
                    Text(
                        "¿Tiene las vacunas al día?",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // motivo de adopción
                OutlinedTextField(
                    value = motivoAdopcion,
                    onValueChange = { motivoAdopcion = it },
                    label = { Text("Motivo de la adopción", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // descripción adicional
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción de la Mascota", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3, // Limita a 3 líneas
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: DATOS DE CONTACTO ---
        Card(
            modifier = Modifier.fillMaxWidth(), // Ocupa el ancho disponible
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Color de fondo de la Card
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)) // Borde de la Card
        ) {
            Column(modifier = Modifier.padding(16.dp)) { // el paddin separa el contenido del borde
                Text(
                    "Datos de Contacto",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Nombre Contacto
                OutlinedTextField(
                    value = nombreContacto,
                    onValueChange = { nombreContacto = it },
                    label = { Text("Nombre Contacto*", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Teléfono Contacto
                OutlinedTextField(
                    value = telefonoContacto,
                    onValueChange = { telefonoContacto = it.filter { c -> c.isDigit() } },
                    label = { Text("Teléfono Contacto*", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: FOTO ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Color de fondo de la Card
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)) // eL color podemos cambiarlo en otro commit jeje
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Foto de la Mascota",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))

                photoUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Foto previsualización",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )
                    Text(
                        "✓ Foto lista",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (photoUri == null) "Tomar Foto*" else "Cambiar Foto",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÓN GUARDAR ---
        Button(
            onClick = {
                // Validación de campos obligatorios , dsp tenemos que agregar la advertencia en grande
                when {
                    nombre.isBlank() -> Toast.makeText(context, "El Nombre es obligatorio", Toast.LENGTH_LONG).show() // Mensaje de error si el nombre está vacío
                    tipoMascota.isBlank() -> Toast.makeText(context, "El Tipo de Animal es obligatorio", Toast.LENGTH_LONG).show() // Mensaje de error si el tipo de animal no está seleccionado
                    photoUri == null -> Toast.makeText(context, "La Foto es obligatoria", Toast.LENGTH_LONG).show() // Mensaje de error si no se ha tomado una foto
                    nombreContacto.isBlank() -> Toast.makeText(context, "El Nombre de Contacto es obligatorio", Toast.LENGTH_LONG).show() // Mensaje de error si el nombre de contacto está vacío
                    telefonoContacto.isBlank() -> Toast.makeText(context, "El Teléfono de Contacto es obligatorio", Toast.LENGTH_LONG).show()// Mensaje de error si el teléfono de contacto está vacío

                    else -> {
                        val nuevaMascota = Mascota(
                            nombre = nombre,
                            raza = raza,
                            descripcion = descripcion,
                            tipo = tipoMascota,
                            edad = edad.toIntOrNull() ?: 0,
                            vacunasAlDia = vacunasAlDia,
                            motivoAdopcion = motivoAdopcion,
                            nombreContacto = nombreContacto,
                            telefonoContacto = telefonoContacto,
                            fotoUri = photoUri.toString()
                        )
                        scope.launch { viewModel.agregarMascota(nuevaMascota) } // Guarda la nueva mascota en la base de datos
                        Toast.makeText(context, "Mascota ${nuevaMascota.nombre} guardada", Toast.LENGTH_SHORT).show() // Mensaje de éxito
                        navController.popBackStack() // Regresa a la pantalla anterior
                    }
                }
            },
            enabled = nombre.isNotBlank() && tipoMascota.isNotBlank() && photoUri != null && nombreContacto.isNotBlank() && telefonoContacto.isNotBlank(), // Habilita el botón solo si los campos obligatorios están llenos
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                "GUARDAR MASCOTA",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}