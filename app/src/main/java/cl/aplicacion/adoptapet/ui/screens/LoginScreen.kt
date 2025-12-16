package cl.aplicacion.adoptapet.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cl.aplicacion.adoptapet.viewmodel.AuthViewModel
import cl.aplicacion.adoptapet.viewmodel.LoginState

@Composable
fun LoginScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val loginState by authViewModel.loginState.collectAsState()
    val context = LocalContext.current

    var pantallaActual by remember { mutableIntStateOf(0) }

    // Variables del formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                val mensaje = if (pantallaActual == 2) "¡Cuenta creada! Bienvenido." else "¡Bienvenido!"

                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()

                navController.navigate("feed") {
                    popUpTo("login") { inclusive = true }
                }
                authViewModel.resetState()
            }
            is LoginState.Error -> {
                val error = (loginState as LoginState.Error).mensaje
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                authViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // LOGO (Simple y limpio)
            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🐾", fontSize = 48.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "AdoptaPet",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // CONTENIDO CENTRAL
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (pantallaActual) {
                    0 -> PantallaInicio(
                        onLoginClick = { pantallaActual = 1 },
                        onRegistroClick = { pantallaActual = 2 }
                    )
                    1 -> FormularioLogin(
                        correo = correo,
                        onCorreoChange = { correo = it },
                        password = password,
                        onPasswordChange = { password = it },
                        isLoading = loginState is LoginState.Loading,
                        onIngresar = {
                            // Limpieza crítica: trim y lowercase
                            authViewModel.login(correo.trim().lowercase(), password.trim())
                        },
                        onVolver = {
                            pantallaActual = 0
                            authViewModel.resetState()
                        }
                    )
                    2 -> FormularioRegistro(
                        nombre = nombre,
                        onNombreChange = { nombre = it },
                        correo = correo,
                        onCorreoChange = { correo = it },
                        password = password,
                        onPasswordChange = { password = it },
                        isLoading = loginState is LoginState.Loading,
                        onRegistrar = {
                            // Limpieza crítica
                            authViewModel.registro(nombre.trim(), correo.trim().lowercase(), password.trim())
                        },
                        onVolver = {
                            pantallaActual = 0
                            authViewModel.resetState()
                        }
                    )
                }
            }
        }
    }
}

// --- COMPONENTES UI ---

@Composable
fun PantallaInicio(onLoginClick: () -> Unit, onRegistroClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Bienvenido a la comunidad de adopción.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onRegistroClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FormularioLogin(
    correo: String, onCorreoChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onIngresar: () -> Unit,
    onVolver: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) { Icon(Icons.Default.ArrowBack, "Volver") }
                Text("Iniciar Sesión", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correo, onValueChange = onCorreoChange,
                label = { Text("Correo") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password, onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = onIngresar,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ingresar")
                }
            }
        }
    }
}

@Composable
fun FormularioRegistro(
    nombre: String, onNombreChange: (String) -> Unit,
    correo: String, onCorreoChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onRegistrar: () -> Unit,
    onVolver: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) { Icon(Icons.Default.ArrowBack, "Volver") }
                Text("Crear Cuenta", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre, onValueChange = onNombreChange,
                label = { Text("Usuario") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = correo, onValueChange = onCorreoChange,
                label = { Text("Correo") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password, onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = onRegistrar,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Registrarse")
                }
            }
        }
    }
}