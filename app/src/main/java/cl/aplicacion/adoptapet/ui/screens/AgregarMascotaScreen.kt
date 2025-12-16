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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import cl.aplicacion.adoptapet.utils.SessionManager
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
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val usuarioActual = SessionManager.correoUsuario ?: SessionManager.nombreUsuario ?: "Usuario Anónimo"

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

    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var tipoMascota by remember { mutableStateOf("Perro") }
    var edad by remember { mutableStateOf("") }
    var vacunasAlDia by remember { mutableStateOf(false) }
    var motivoAdopcion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val tiposDeAnimales = listOf("Perro", "Gato", "Conejo", "Pájaro", "Otro")

    var nombreContacto by remember { mutableStateOf("") }
    var telefonoContacto by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Agregar Nueva Mascota",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Publicando como: $usuarioActual",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre*", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

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

                Text("Tipo de Animal*:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Column(Modifier.fillMaxWidth()) {
                    tiposDeAnimales.forEach { tipo ->
                        Row(
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
                            Text(tipo, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it.filter { c -> c.isDigit() } },
                    label = { Text("Edad (años)", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vacunasAlDia = !vacunasAlDia }
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(checked = vacunasAlDia, onCheckedChange = { vacunasAlDia = it })
                    Text("¿Tiene las vacunas al día?", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(12.dp))

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

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción de la Mascota", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Datos de Contacto",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
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

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
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
                        modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
                    )
                    Text("✓ Foto lista", color = Color.Green, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (photoUri == null) "Tomar Foto*" else "Cambiar Foto", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    nombre.isBlank() -> Toast.makeText(context, "El Nombre es obligatorio", Toast.LENGTH_LONG).show()
                    tipoMascota.isBlank() -> Toast.makeText(context, "El Tipo de Animal es obligatorio", Toast.LENGTH_LONG).show()
                    photoUri == null -> Toast.makeText(context, "La Foto es obligatoria", Toast.LENGTH_LONG).show()
                    nombreContacto.isBlank() -> Toast.makeText(context, "El Nombre de Contacto es obligatorio", Toast.LENGTH_LONG).show()
                    telefonoContacto.isBlank() -> Toast.makeText(context, "El Teléfono de Contacto es obligatorio", Toast.LENGTH_LONG).show()

                    else -> {
                        val creadorParaGuardar = SessionManager.correoUsuario ?: SessionManager.nombreUsuario ?: "Anónimo"

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
                            fotoUri = photoUri.toString(),
                            creador = creadorParaGuardar
                        )

                        scope.launch { viewModel.agregarMascota(nuevaMascota) }
                        Toast.makeText(context, "Mascota guardada por $creadorParaGuardar", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            },
            enabled = nombre.isNotBlank() && tipoMascota.isNotBlank() && photoUri != null && nombreContacto.isNotBlank() && telefonoContacto.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("GUARDAR MASCOTA", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}