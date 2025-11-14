package com.example.baseproject.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.baseproject.viewmodel.AnimeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//Prueba simple de puro texto para mostrar animes
@Composable
fun animeListScreen(
) {
    val viewModel: AnimeViewModel = viewModel()
    val listState by viewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadList(limit = 5)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Prueba de animes")
        Spacer(modifier = Modifier.height(16.dp))

        if(listState.isLoading) {
            Text("Cargando bro")
        }
        else if(listState.error != null) {
            Text("Error triste :( ${listState.error}")
        }
        else {
            Text("Datos conseguidos :D ${listState.animes.size} animes conseguidos")
            Spacer(modifier = Modifier.height(16.dp))
            listState.animes.forEach { anime ->
                Text("${anime.attributes.canonicalTitle}")
                Text("${anime.attributes.description}")
                Text("${anime.attributes.ageRating}")
                Text("${anime.attributes.status}")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
