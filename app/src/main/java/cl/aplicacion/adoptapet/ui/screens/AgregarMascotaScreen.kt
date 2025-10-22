package cl.aplicacion.adoptapet.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun AgregarMascotaScreen() {

    // --- 1. LÓGICA DE LA CÁMARA (Recurso Nativo 1) ---

    // Prepara una "caja" vacía (null) para guardar la foto. 'remember' y 'mutableStateOf' le dicen a Compose: "vigila esta variable, si cambia, redibuja la UI".
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    // Prepara el "lanzador" (el "mensajero" que va a buscar la foto).
    val launcher = rememberLauncherForActivityResult(
        // Le decimos que su trabajo es 'TakePicturePreview()' (sacar foto, sin guardar, sin permisos).
        contract = ActivityResultContracts.TakePicturePreview()
    ) { fotoBitmap ->
        // Guardamos la 'fotoBitmap' en nuestra "caja". Compose ve el cambio y redibuja.
        bitmap = fotoBitmap
    }

    //PARA EL FORMULARIO
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // DISEÑO DE LA PANTALLA (UI)
    Column(
        modifier = Modifier
            .fillMaxSize() // Usa toda la pantalla
            .padding(16.dp), // Margen en los bordes
        horizontalAlignment = Alignment.CenterHorizontally, // Centra todo horizontalmente
        verticalArrangement = Arrangement.Center // Centra todo verticalmente
    ) {

        Text("Agregar Nueva Mascota", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical

        // EL FORMULARIO
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la Mascota") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = raza,
            onValueChange = { raza = it },
            label = { Text("Raza") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción (ej: vacunas, motivo)") }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            launcher.launch()
        }) {
            Text("Tomar Foto")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 5. EL VISOR DE LA FOTO
        // 'bitmap?.let' significa: "Si la 'caja' bitmap NO está vacía (null)..."
        bitmap?.let { fotoTomada ->
            Text("¡Foto capturada!")
            Image(
                bitmap = fotoTomada.asImageBitmap(),
                contentDescription = "Foto de la mascota",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}