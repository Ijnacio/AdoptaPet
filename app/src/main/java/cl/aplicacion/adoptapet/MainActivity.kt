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
        // enableEdgeToEdge() permite que tu app use la pantalla completa
        enableEdgeToEdge()

        // setContent es donde empieza la UI de Jetpack Compose
        setContent {
            // 1. Carga tu tema (AdoptaPetTheme).
            //    adentro usará tus colores pasteles del archivo ui/theme/Color.kt
            AdoptaPetTheme {
                // 2. Aquí llamas a tu Navegación.
                // Le entregas el control a AppNavigation, que decidirá
                // qué pantalla mostrar (primero "feed").
                AppNavigation()
            }
        }
    }
}