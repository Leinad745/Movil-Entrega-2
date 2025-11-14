package com.example.baseproject.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.baseproject.viewmodel.AnimeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.baseproject.model.Anime

@Composable
fun animeListScreen(
) {
    val viewModel: AnimeViewModel = viewModel()
    val listState by viewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadList(limit =20, offset = 20)
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(listState.animes){ anime ->
                    animeCard(anime = anime)
                }
            }
        }
    }
}

@Composable
fun animeCard(anime: Anime) {
    Card(
       modifier = Modifier.fillMaxWidth().aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column() {
            AsyncImage(
                model = anime.attributes.posterImage.original,
                contentDescription = "Poster del anime: ${anime.attributes.canonicalTitle}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Text(
               text = anime.attributes.canonicalTitle,
                modifier = Modifier.padding(8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



