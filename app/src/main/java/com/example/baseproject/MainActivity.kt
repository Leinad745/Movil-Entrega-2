package com.example.baseproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.baseproject.data.AnimeRepository
import com.example.baseproject.model.PosterImage
import com.example.baseproject.ui.screens.WelcomeScreen
import com.example.baseproject.ui.theme.BaseAndroidProjectTheme
import com.example.baseproject.view.animeListScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
*Esto es una prueba para comprobar la conexion con la api kitsu y validar
* que trae todos los datos que declaramos en el model.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseAndroidProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    animeListScreen()
                }
            }
        }
    }
}