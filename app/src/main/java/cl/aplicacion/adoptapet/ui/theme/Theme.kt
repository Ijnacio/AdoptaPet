package cl.aplicacion.adoptapet.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // <-- Import para Color.Black/White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColorScheme(
    primary = NaranjoFuerte,
    secondary = VerdeFuerte,
    tertiary = VerdeAgua,
    background = TextoOscuro,
    surface = GrisClaro,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = TextoOscuro,
)

private val LightColorPalette = lightColorScheme(
    primary = NaranjoFuerte,
    secondary = VerdeFuerte,
    tertiary = VerdeAgua,
    background = NaranjoPastel,
    surface = GrisClaro,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = TextoOscuro,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro,
)

@Composable
fun AdoptaPetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, // Aplica la paleta elegida
        typography = Typography,   // Usa las fuentes de Type.kt
        content = content
    )
}