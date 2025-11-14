package com.example.baseproject.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.baseproject.viewmodel.RegViewModel
import androidx.compose.runtime.livedata.observeAsState


/*

HAY QUE IMPLEMENTAR PRIMERO EL NAVIGATOR

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
            // navController.navigate("pantalla_destino") {
            //     popUpTo("registro") { inclusive = true }
            // }
            //
            return true
        }
    }



@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Registro(navController = rememberNavController())
}*/