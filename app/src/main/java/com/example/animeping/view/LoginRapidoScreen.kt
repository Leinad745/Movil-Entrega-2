package com.example.animeping.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.animeping.viewmodel.RegViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registroRapido(
    navController: NavController,
    regViewModel: RegViewModel? = null
) {
    val sharedRegViewModel = regViewModel ?: viewModel()
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }

    val errorLogin by sharedRegViewModel.errorLogin.observeAsState()
    val autenticado by sharedRegViewModel.autenticado.observeAsState(false)
    val usuarioActual by sharedRegViewModel.usuarioActual.observeAsState()

    LaunchedEffect(Unit) {
        sharedRegViewModel.refrescarUsuario()
    }

    LaunchedEffect(autenticado) {
        if (autenticado == true) {
            /*
            * AÑADIR NAVEGACION A PANTALLA PRINCIPAL
            * */
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Iniciar Sesión") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Hola de nuevo!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            usuarioActual?.let { user ->
                Text(
                    text = "Bienvenido, ${user.nombreUsuario}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    sharedRegViewModel.limpiarError()
                },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Icon(
                            imageVector = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                singleLine = true
            )

            errorLogin?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    sharedRegViewModel.login(contrasena)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = contrasena.isNotBlank()
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    /*
                    navController.navigate(AppScreen.Login.route)
                    sharedAuthViewModel.clearError()*/
                }
            ) {
                val label = usuarioActual?.let { user -> "No soy ${user.nombreUsuario}" } ?: "Iniciar con otra cuenta"
                Text(label)
            }
        }
    }
}
