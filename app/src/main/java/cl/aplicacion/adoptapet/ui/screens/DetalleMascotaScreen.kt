package cl.aplicacion.adoptapet.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun DetalleMascotaScreen() {
    // LÓGICA DE COMPARTIR
    // 1. Obtenemos el "Contexto"
    val context = LocalContext.current

    // 2. Simulamos los datos de la mascota
    val nombreMascota = "Fido"
    val razaMascota = "Kiltro"

    //DISEÑO DE LA PANTALLA
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Detalle de: $nombreMascota", style = MaterialTheme.typography.titleLarge)
        Text("Raza: $razaMascota", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        //  FORMULARIO DE ADOPCIÓN
        Text("Aquí irá tu formulario de adopción", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN "COMPARTIR" (IE 2.4.1) ---
        Button(onClick = {

            // 3. Prepara el "Intent" (la orden) para compartir
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND // La acción es "Enviar/Compartir"
                // El contenido que vamos a compartir
                putExtra(Intent.EXTRA_TEXT, "¡Mira esta mascota para adoptar! Se llama $nombreMascota ($razaMascota).")
                type = "text/plain" // Le decimos que es texto plano
            }

            // 4. Crea el "Selector" (el menú que te deja elegir app)
            val shareIntent = Intent.createChooser(sendIntent, "Compartir Mascota")

            // 5. Lanza la actividad (esto abre el menú de WhatsApp, IG, etc.)
            context.startActivity(shareIntent)

        }) {
            Text("Compartir con un Amigo")
        }
    }
}