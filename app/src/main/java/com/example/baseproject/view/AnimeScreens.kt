package com.example.baseproject.view

import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.baseproject.viewmodel.AnimeViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.baseproject.model.Anime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.livedata.observeAsState
import com.example.baseproject.viewmodel.RegViewModel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    onAnimeClickScreen: (String) -> Unit,
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val viewModel: AnimeViewModel = viewModel()
    val listState by viewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadList(limit = 20, offset = 0)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Anime Ping!") }, actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Buscar"
                    )
                }
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = "Perfil"
                    )
                }
            })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when {
                listState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando Animes...")
                        }
                    }
                }
                listState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${listState.error}")
                    }
                }
                else -> {
                    Text(
                        "Animes cargados correctamente",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listState.animes) { anime ->
                            AnimeCard(
                                anime = anime,
                                onClick = {
                                    onAnimeClickScreen(anime.id)
                                }
                            )
                        }

                        if (listState.hasMoreData) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                                LaunchedEffect(Unit) {
                                    viewModel.loadMore()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun AnimeCard(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable { onClick() },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeScreen(
    animeId: String,
    onBack: () -> Unit
) {

    val viewModel: AnimeViewModel = viewModel()
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetail(animeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle anime")},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Volver"

                        )
                    }
                }
            )
        }
    ) { innerPadding->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
        when {
            detailState.isLoading -> {
                Box(modifier =  Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Text("Cargando detalles...", modifier = Modifier.padding(top = 16.dp))
                    }
                }
            }
            detailState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${detailState.error}")
                        androidx.compose.material3.Button(
                            onClick = { viewModel.loadAnimeDetail(animeId) },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            detailState.anime != null -> {
                val anime = detailState.anime!!
                AnimeDetailContent(anime = anime)
            }
        }
    }
}


@Composable
fun AnimeDetailContent(anime: Anime) {
    val viewModel: AnimeViewModel = viewModel()
    val regViewModel: RegViewModel = viewModel()

    val currentUser by regViewModel.usuarioActual.observeAsState()

    val isFavorite = remember(currentUser, anime.id) {
        currentUser?.favoritos?.contains(anime.id) == true
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                AsyncImage(
                    model = anime.attributes.posterImage.original,
                    contentDescription = anime.attributes.canonicalTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Title takes up available space
                    Text(
                        text = anime.attributes.canonicalTitle,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        viewModel.toggleFavorite(anime.id)
                        regViewModel.updateUser()
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📺 ${anime.attributes.episodeCount} eps")
                    Text("🏷️ ${anime.attributes.status}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = anime.attributes.description,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Detalles adicionales
                Text(
                    text = "Detalles",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column {
                    Text("Clasificación: ${anime.attributes.ageRating ?: "N/A"}")
                    Text("Inicio: ${anime.attributes.startDate}")
                    Text("Fin: ${anime.attributes.endDate}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearchScreen(
    onBack: () -> Unit,
    onAnimeClick: (String) -> Unit
) {
    val viewModel: AnimeViewModel = viewModel()
    val searchState by viewModel.searchState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var debounceJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { newQuery ->
                            searchQuery = newQuery

                            debounceJob?.cancel()

                            debounceJob = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                kotlinx.coroutines.delay(500)
                                if (newQuery.isNotEmpty() && newQuery.length >= 3) {
                                    viewModel.searchAnimeByName(newQuery)
                                } else if (newQuery.isEmpty()) {
                                    viewModel.searchAnimeByName("")
                                }
                            }
                        },
                        placeholder = { Text("Buscar anime...") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                        },
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when {
                searchState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Buscando animes...")
                        }
                    }
                }

                searchState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: ${searchState.error}")
                            Spacer(modifier = Modifier.height(16.dp))
                            androidx.compose.material3.Button(
                                onClick = {
                                    if (searchQuery.isNotEmpty()) {
                                        viewModel.searchAnimeByName(searchQuery)
                                    }
                                }
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                searchQuery.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Escribe el nombre de un anime para buscar")
                    }
                }

                searchQuery.length < 3 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Escribe al menos 3 caracteres")
                    }
                }

                searchState.animes.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se encontraron animes para '$searchQuery'")
                    }
                }

                else -> {
                    Text(
                        text = "Resultados para $searchQuery:",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(searchState.animes) { anime ->
                            AnimeCard(
                                anime = anime,
                                onClick = { onAnimeClick(anime.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}