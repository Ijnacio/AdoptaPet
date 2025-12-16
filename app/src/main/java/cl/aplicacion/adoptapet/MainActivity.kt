package cl.aplicacion.adoptapet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.aplicacion.adoptapet.ui.theme.AdoptaPetTheme
import cl.aplicacion.adoptapet.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AdoptaPetTheme {
                AppNavigation()
            }
        }
    }
}