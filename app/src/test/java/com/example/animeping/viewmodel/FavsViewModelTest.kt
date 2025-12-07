package com.example.animeping.viewmodel

import com.example.animeping.data.AnimeRepository
import com.example.animeping.data.UserRepository
import com.example.animeping.model.Anime
import com.example.animeping.model.AnimeAttributes
import com.example.animeping.model.PosterImage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavsViewModelTest {
    private lateinit var viewModel: FavsViewModel

    private val animeRepository = mockk<AnimeRepository>()
    private val userRepository = mockk<UserRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = spyk(FavsViewModel()) {
            this::class.java.getDeclaredField("animeRepository").apply {
                isAccessible = true
                set(this@spyk, animeRepository)
            }
        }
        mockkObject(UserRepository)
        every { UserRepository.getInstance(any()) } returns userRepository
    }

    @Test
    fun `Si no hay favoritos, uiState tiene lista vacia`() = runTest {
        every { userRepository.getFavorites() }  returns emptyList()

        viewModel.loadFavoriteAnimes(mockk(relaxed = true))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state.favoriteAnimes.isEmpty())
        assert(!state.isLoading)
        assert(state.error == null)
    }
    @Test
    fun `Cuando hay favoritos, uiState recibe lista de animes`() = runTest {
        every { userRepository.getFavorites() } returns listOf("1", "1")

        coEvery { animeRepository.getAnimeById("1") } returns mockk(relaxed = true)
        coEvery { animeRepository.getAnimeById("1") } returns mockk(relaxed = true)

        viewModel.loadFavoriteAnimes(mockk(relaxed = true))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assert(state.favoriteAnimes.size == 2)
        assert(!state.isLoading)
        assert(state.error == null)
    }

    @Test
    fun `Cuando hay favoritos uiState refleja los animes cargados`() = runTest {
        val favoriteIds = listOf("1", "2")
        coEvery { userRepository.getFavorites() } returns favoriteIds

        val anime1 = Anime(
            id = "1",
            type = "anime",
            attributes = AnimeAttributes(
                description = "Descripcion de Naruto",
                canonicalTitle = "Naruto",
                status = "finished",
                episodeCount = 220,
                ageRating = "PG",
                userCount = 1000000,
                startDate = "2002-10-03",
                endDate = "2007-02-08",
                nextRelease = null,
                posterImage = PosterImage(
                    tiny = "urlTiny1",
                    large = "urlLarge1",
                    small = "urlSmall1",
                    medium = "urlMedium1",
                    original = "urlOriginal1"
                )
            )
        )

        val anime2 = Anime(
            id = "2",
            type = "anime",
            attributes = AnimeAttributes(
                description = "Descripcion de One Piece",
                canonicalTitle = "One Piece",
                status = "ongoing",
                episodeCount = 1050,
                ageRating = "PG-13",
                userCount = 2000000,
                startDate = "1999-10-20",
                endDate = "",
                nextRelease = "2025-12-13",
                posterImage = PosterImage(
                    tiny = "urlTiny2",
                    large = "urlLarge2",
                    small = "urlSmall2",
                    medium = "urlMedium2",
                    original = "urlOriginal2"
                )
            )
        )

        coEvery { animeRepository.getAnimeById("1") } returns anime1
        coEvery { animeRepository.getAnimeById("2") } returns anime2

        viewModel.loadFavoriteAnimes(mockk(relaxed = true))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assert(state.favoriteAnimes.size == 2)
        assert(state.favoriteAnimes.containsAll(listOf(anime1, anime2)))
        assert(!state.isLoading)
        assert(state.error == null)
    }

    @Test
    fun `Cuando favoritos tienen campos opcionales nulos, uiState los maneja correctamente`() = runTest {
        val favoriteIds = listOf("3")
        coEvery { userRepository.getFavorites() } returns favoriteIds

        val animeWithNullFields = Anime(
            id = "3",
            type = "anime",
            attributes = AnimeAttributes(
                description = "Anime sin campos opcionales",
                canonicalTitle = "Null Fields Anime",
                status = "finished",
                episodeCount = 12,
                ageRating = null,
                userCount = 12345,
                startDate = "2020-01-01",
                endDate = "2020-06-01",
                nextRelease = null,
                posterImage = PosterImage(
                    tiny = "tiny3",
                    large = "large3",
                    small = "small3",
                    medium = "medium3",
                    original = "original3"
                )
            )
        )

        coEvery { animeRepository.getAnimeById("3") } returns animeWithNullFields

        viewModel.loadFavoriteAnimes(mockk(relaxed = true))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assert(state.favoriteAnimes.size == 1)
        val animeLoaded = state.favoriteAnimes.first()

        assert(animeLoaded.attributes.ageRating == null)
        assert(animeLoaded.attributes.nextRelease == null)
        assert(!state.isLoading)
        assert(state.error == null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}