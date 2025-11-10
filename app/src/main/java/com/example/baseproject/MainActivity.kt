package com.example.baseproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.baseproject.data.AnimeRepository
import com.example.baseproject.ui.screens.WelcomeScreen
import com.example.baseproject.ui.theme.BaseAndroidProjectTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
*Esto es una prueba para comprobar la conexion con la api kitsu y validar
* que trae todos los datos que declaramos en el model.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Probar la conexión
        testConnection()

        setContent {
            BaseAndroidProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreen()
                }
            }
        }
    }

    private fun testConnection() {
        GlobalScope.launch {
            try {
                val repo = AnimeRepository()
                val result = repo.getAnimeList(limit = 1, offset = 0)
                println("✅ CONEXIÓN EXITOSA")
                println(result)
                println("Animes obtenidos: ${result.data?.size ?: 0}")

            } catch (e: Exception) {
                println("❌ ERROR: ${e.message}")
            }
        }
    }
}