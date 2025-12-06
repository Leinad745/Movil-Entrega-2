package com.example.animeping.viewmodel

import android.app.Application
import com.example.animeping.data.AnimeRepository
import com.example.animeping.data.UserRepository
import com.example.animeping.model.Anime
import com.example.animeping.model.AnimeAttributes
import com.example.animeping.model.AnimeListData
import com.example.animeping.model.PosterImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq


//Permite el uso de las funciones suspendidas en las pruebas
@OptIn(ExperimentalCoroutinesApi::class)
class AnimeViewModelTest {
    @get:Rule
    val hiloDePrueba = HiloDePrueba()

    @Mock
    private lateinit var mockAplicacion: Application

    @Mock
    private lateinit var mockAnimeRepo: AnimeRepository

    @Mock
    private lateinit var mockUserRepo: UserRepository

    private lateinit var viewModel: AnimeViewModel

    private lateinit var dummyAnime: Anime

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        val dummyPosters = PosterImage("t","l","s","m", "o")
        val dummyAtributos = AnimeAttributes(
            description = "Diversion con chicas samurai",
            canonicalTitle = "Senran Kagura",
            status = "current",
            episodeCount = 12,
            ageRating = "R",
            userCount = 10,
            startDate = "2020",
            endDate = "2021",
            nextRelease = null,
            posterImage = dummyPosters
        )
        dummyAnime = Anime("1", "anime", dummyAtributos)

        viewModel = AnimeViewModel(mockAplicacion, mockAnimeRepo, mockUserRepo)
    }

    @Test
    fun `Carga de listState con datos`() = runTest {
        val mockRespuesta = AnimeListData(listOf(dummyAnime))
        `when`(mockAnimeRepo.getAnimeList(20,0)).thenReturn(mockRespuesta)

        viewModel.loadList()

        //Avanza el tiempo hasta que se cumpla con el tiempo de espera
        advanceUntilIdle()

        //verficacion
        val currentState = viewModel.listState.value
        assertFalse(currentState.isLoading)
        assertEquals(1, currentState.animes.size)
        assertEquals("Senran Kagura", currentState.animes[0].attributes.canonicalTitle)
        assertNull(currentState.error)
    }

    @Test
    fun `manejo de errores de loadList`() = runTest {
        `when`(mockAnimeRepo.getAnimeList(20,0)).thenThrow(RuntimeException("Error de API"))

        viewModel.loadList()
        advanceUntilIdle()

        val currentState = viewModel.listState.value
        assertFalse(currentState.isLoading)
        assertTrue(currentState.animes.isEmpty())
        assertEquals("Error de API", currentState.error)
    }

    @Test
    fun `toggleFavorite agrega favoritos si no existen`() = runTest{
        `when`(mockUserRepo.getFavorites()).thenReturn(emptyList())

        viewModel.toggleFavorite("1")
        advanceUntilIdle()

        verify(mockUserRepo).addFavorite("1")
    }

    @Test
    fun `toggleFavorite elimina favoritos si existen`()=runTest{
        `when`(mockUserRepo.getFavorites()).thenReturn(listOf("1","55"))

        viewModel.toggleFavorite("1")
        advanceUntilIdle()

        verify(mockUserRepo).removeFavorite("1")

    }

    @Test
    fun `loadAnimeDetail actualiza el estado`() = runTest {
        `when`(mockAnimeRepo.getAnimeById("1")).thenReturn(dummyAnime)

        viewModel.loadAnimeDetail("1")
        advanceUntilIdle()

        val state = viewModel.detailState.value
        assertNotNull(state.anime)
        assertEquals("Senran Kagura", state.anime?.attributes?.canonicalTitle)
        assertFalse(state.isLoading)
    }

    @Test
    fun `searchAnimeByName actualiza el estado`() = runTest {
        val consulta = "Senran Kagura"
        val mockResponse = AnimeListData(listOf(dummyAnime))
        `when`(mockAnimeRepo.getAnimeByAnime(eq(consulta))).thenReturn(mockResponse)

        viewModel.searchAnimeByName(consulta)
        advanceUntilIdle()

        val state = viewModel.searchState.value


        assertNull(state.error)
        assertFalse(state.animes.isEmpty())
        assertEquals(1, state.animes.size)
        assertEquals("Senran Kagura", state.animes[0].attributes.canonicalTitle)
    }

}