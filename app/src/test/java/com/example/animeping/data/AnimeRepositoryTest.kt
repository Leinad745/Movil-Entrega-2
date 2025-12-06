package com.example.animeping.data

import com.example.animeping.data.remote.KitsuApiService
import com.example.animeping.model.Anime
import com.example.animeping.model.AnimeAttributes
import com.example.animeping.model.PosterImage
import com.example.animeping.model.AnimeListData
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`
import org.mockito.ArgumentMatchers.anyInt
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import java.lang.RuntimeException


class AnimeRepositoryTest {
    @Mock
    private lateinit var mockApi: KitsuApiService
    //lateinit: permite inicializar la variable más tarde, pero no con un valor nulo.

    private lateinit var repositorio: AnimeRepository

    private lateinit var dummyAnime: Anime
    private lateinit var dummyAttributes: AnimeAttributes
    private lateinit var dummyPoster: PosterImage


    @Before
    fun before(){
        MockitoAnnotations.openMocks(this)
        repositorio = AnimeRepository(mockApi)

        dummyPoster = PosterImage(
            tiny = "url_tiny",
            large = "url_large",
            small = "url_small",
            medium = "url_medium",
            original = "url_original"
        )

        dummyAttributes = AnimeAttributes(
            description = "Accion de chicas samurai",
            canonicalTitle = "Senran Kagura",
            status = "finished",
            episodeCount = 12,
            ageRating = "T",
            userCount = 1000,
            startDate = "2023-01-01",
            endDate = "2023-01-31",
            nextRelease = null,
            posterImage = dummyPoster
        )

        dummyAnime = Anime(
            id = "1",
            type = "anime",
            attributes = dummyAttributes
        )
    }

    @Test
    fun `getAnimeList retorna la lista correcta`() = runBlocking<Unit> {
        val respuesta = AnimeListData(data = listOf(dummyAnime))

        `when`(mockApi.fetchAnimeList(anyInt(), anyInt())).thenReturn(respuesta)
        val resultado = repositorio.getAnimeList(20, 0)

        assertNotNull(resultado)
        assertEquals(1, resultado.data.size)
        assertEquals("Senran Kagura", resultado.data[0].attributes.canonicalTitle)

        verify(mockApi).fetchAnimeList(20,0)
    }

    @Test
    fun `getAnimeById retorna un anime correcto`() = runBlocking<Unit> {
        val respuesta = AnimeListData(data = listOf(dummyAnime))

        `when`(mockApi.fetchAnimeDetail("1")).thenReturn(respuesta)

        val resultado = repositorio.getAnimeById("1")

        assertNotNull(resultado)
        assertEquals("Senran Kagura", resultado?.attributes?.canonicalTitle)
        assertEquals("1", resultado?.id)
    }

    @Test(expected = Exception::class)
    fun `getAnimeById lanza una excepcion en caso de fallo`() = runBlocking<Unit> {
        `when`(mockApi.fetchAnimeDetail(anyString())).thenThrow(RuntimeException("Error 500"))

        repositorio.getAnimeById("1")
    }

    @Test
    fun `getAnimeByName busca por nombre correctamente`() = runBlocking<Unit>{
        val busqueda = "Senran Kagura"
        val respuesta = AnimeListData(data = listOf(dummyAnime))

        `when`(mockApi.fetchAnimesByName(busqueda)).thenReturn(respuesta)

        val resultado = repositorio.getAnimeByAnime(busqueda)

        assertEquals(respuesta, resultado)
        verify(mockApi).fetchAnimesByName(busqueda)
    }
}