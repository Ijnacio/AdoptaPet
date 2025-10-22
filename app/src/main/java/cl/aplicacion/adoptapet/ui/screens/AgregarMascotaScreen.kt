package cl.aplicacion.adoptapet.ui.screens

import android.Manifest // Necesario para el permiso de cámara
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // Import genérico para Layouts
import androidx.compose.foundation.rememberScrollState // Para hacer scroll si el contenido es largo
import androidx.compose.foundation.verticalScroll // Para hacer scroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.* // Import genérico para Runtime (remember, mutableStateOf, etc.)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.model.entities.Mascota
import cl.aplicacion.adoptapet.viewmodel.FormularioViewModel
import kotlinx.coroutines.launch
import androidx.core.content.FileProvider // Necesario para crear la URI segura
import coil.compose.rememberAsyncImagePainter // Necesario para mostrar la foto desde URI
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

    // --- 1. LÓGICA DE LA CÁMARA (CON MANEJO DE PERMISOS) ---

    // Variable para guardar el "link" (URI) a la foto. Inicia vacía (null).
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current // Contexto necesario para Toast y FileProvider

    // Función auxiliar para crear un archivo temporal y obtener su URI segura
    fun getTmpFileUri(): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        // Directorio temporal privado de la app
        val storageDir: File? = context.getExternalFilesDir(null)
        val tmpFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            // Asegura que el archivo exista y se borre al salir (opcional)
            createNewFile()
            deleteOnExit()
        }
        // Crea una URI segura tipo "content://" usando FileProvider
        return FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            // IMPORTANTE: Esta "dirección" debe coincidir con la del AndroidManifest.xml
            "${context.packageName}.provider",
            tmpFile
        )
    }

    // "Launcher" para ABRIR LA CÁMARA y guardar la foto en la URI que le pasemos
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(), // Contrato que guarda la foto
        onResult = { success -> // Resultado: ¿Se guardó la foto?
            if (success) {
                // Si sí, 'photoUri' ya tiene el link correcto. La UI se actualizará sola.
                Toast.makeText(context, "Foto capturada", Toast.LENGTH_SHORT).show()
            } else {
                // Si el usuario canceló, limpiamos la URI
                Toast.makeText(context, "Captura cancelada", Toast.LENGTH_SHORT).show()
                photoUri = null
            }
        }
    )

    // "Launcher" para PEDIR PERMISO de cámara
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), // Contrato para pedir UN permiso
        onResult = { isGranted: Boolean -> // Resultado: ¿Dio permiso?
            if (isGranted) {
                // SI dio permiso: Creamos la URI y AHORA SÍ lanzamos la cámara
                val uri = getTmpFileUri()
                photoUri = uri // Guardamos la URI para mostrarla y usarla al guardar
                takePictureLauncher.launch(uri) // Lanzamos el OTRO launcher
            } else {
                // NO dio permiso: Mostramos mensaje
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // --- VARIABLES PARA EL ESTADO DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    // (Añade aquí las otras variables: edad, vacunas (Boolean), motivo, etc.)

    // --- SCOPE PARA LA CORUTINA DE GUARDADO ---
    val scope = rememberCoroutineScope() // Necesario para llamar a funciones 'suspend'

    // --- DISEÑO DE LA PANTALLA (UI) ---
    // Usamos verticalScroll para que el formulario se pueda desplazar si no cabe
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Permite hacer scroll
        horizontalAlignment = Alignment.CenterHorizontally
        // Quitamos verticalArrangement.Center para que el scroll funcione bien
    ) {

        Text("Agregar Nueva Mascota", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))

        // --- EL FORMULARIO ---
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = raza,
            onValueChange = { raza = it },
            label = { Text("Raza") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        // (Añade aquí los otros OutlinedTextField para edad, motivoAdopcion, etc.)
        // (Añade un Checkbox para vacunasAlDia)

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÓN DE CÁMARA (Llama al launcher de permisos) ---
        Button(onClick = {
            // Pide permiso. Si lo dan, el 'requestPermissionLauncher' llamará a la cámara.
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text("Tomar Foto")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- BOTÓN DE GUARDAR (Llama al ViewModel) ---
        Button(
            onClick = {
                // Validación simple (puedes mejorarla)
                if (nombre.isBlank()) {
                    Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                } else if (photoUri == null) {
                    Toast.makeText(context, "La foto es obligatoria", Toast.LENGTH_SHORT).show()
                } else {
                    // 1. Crea el objeto Mascota con los datos del formulario
                    val nuevaMascota = Mascota(
                        // ID es 0 porque Room lo genera
                        nombre = nombre,
                        raza = raza,
                        descripcion = descripcion,
                        fotoUri = photoUri.toString(), // ¡Guarda la URI como texto!
                        edad = 2, // Reemplaza con el valor del TextField de edad
                        vacunasAlDia = true, // Reemplaza con el valor del Checkbox
                        motivoAdopcion = "Encontrado", // Reemplaza con el TextField
                        nombreContacto = "Dueño de Prueba", // Reemplaza con TextField
                        telefonoContacto = "912345678" // Reemplaza con TextField
                    )

                    // 2. Llama al ViewModel para guardar (en segundo plano)
                    scope.launch {
                        viewModel.agregarMascota(nuevaMascota)
                    }

                    // 3. Muestra mensaje de éxito
                    Toast.makeText(context, "Mascota Guardada", Toast.LENGTH_SHORT).show()

                    // 4. Vuelve a la pantalla anterior (Feed)
                    navController.popBackStack()

                    // (La limpieza de campos ya no es necesaria aquí porque volvemos atrás)
                }
            },
            // Deshabilita el botón si no se ha tomado la foto
            enabled = photoUri != null
        ) {
            Text("Guardar Mascota")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- VISOR DE LA FOTO CAPTURADA ---
        // Muestra la imagen si 'photoUri' no es null
        photoUri?.let { uri ->
            Text("Foto lista para guardar:")
            Image(
                // Coil carga la imagen desde la URI (link al archivo temporal)
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Foto de la mascota",
                modifier = Modifier.size(200.dp) // Tamaño de la previsualización
            )
        }
    } // Fin Column
}
