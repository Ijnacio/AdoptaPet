package cl.aplicacion.adoptapet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// Importa el "Tema" de tu app (donde están tus colores pasteles)
import cl.aplicacion.adoptapet.ui.theme.AdoptaPetTheme
import cl.aplicacion.adoptapet.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // permite que tu app use la pantalla completa
        enableEdgeToEdge()

        // donde empieza la UI de Compose
        setContent {
            //  se usa el Tema de la app
            AdoptaPetTheme {
                //  se llama a la navegación principal
                AppNavigation()
            }
        }
    }
}