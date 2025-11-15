package com.example.baseproject.view

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baseproject.viewmodel.RegViewModel

//HAY QUE IMPLEMENTAR PRIMERO EL NAVIGATOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registro(
    navController: NavController,
    regViewModel: RegViewModel? = null
) {
    val sharedAuthViewModel = regViewModel ?: viewModel()
    var mail by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }
    var confirmarContrasenaVisible by remember { mutableStateOf(false) }

    val errorLogin by sharedAuthViewModel.errorLogin.observeAsState()
    val registroExitoso by sharedAuthViewModel.registroExitoso.observeAsState(false)

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            //navController.navigate(AppScre) {
            //     popUpTo("registro") { inclusive = true }
            // }
            //
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crea Tu Cuenta") })
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
                text = "Registrarse",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = {
                    nombreUsuario = it
                    sharedAuthViewModel.limpiarError()
                },
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = mail,
                onValueChange = {
                    mail = it
                    sharedAuthViewModel.limpiarError()
                },
                label = { Text("Correo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    sharedAuthViewModel.limpiarError()
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

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = {
                    confirmarContrasena = it
                    sharedAuthViewModel.limpiarError()
                },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = if (confirmarContrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmarContrasenaVisible = !confirmarContrasenaVisible }) {
                        Icon(
                            imageVector = if (confirmarContrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmarContrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña"
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

            if (contrasena != confirmarContrasena && confirmarContrasena.isNotEmpty()) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    if (contrasena == confirmarContrasena) {
                        sharedAuthViewModel.registroUsuario(nombreUsuario, mail, contrasena)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = nombreUsuario.isNotBlank() && contrasena.isNotBlank() && contrasena == confirmarContrasena
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 18.sp
                )
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroPreview() {
    MaterialTheme {
        Registro(navController = rememberNavController())
    }
}
*/