package com.example.animeping.viewmodel

import android.app.Application
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.animeping.data.UserRepository
import com.example.animeping.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TestPerfilViewModel{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockApplication: Application
    private lateinit var mockUserRepository: UserRepository
    private lateinit var viewModel: PerfilViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockApplication = mock()
        mockUserRepository = mock()

        viewModel = mock(defaultAnswer = Answers.CALLS_REAL_METHODS)

        val repoField = PerfilViewModel::class.java.getDeclaredField("repositorio")
        repoField.isAccessible = true
        repoField.set(viewModel, mockUserRepository)

        val uiStateField = PerfilViewModel::class.java.getDeclaredField("_uiState")
        uiStateField.isAccessible = true

        val mutableUiState = MutableStateFlow(PerfilUiState())

        uiStateField.set(viewModel, mutableUiState)

        whenever(viewModel.uiState).thenReturn(mutableUiState.asStateFlow())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test init - carga la imagen del usuario al inicializar`() = runTest {
        val fakeImageUri: Uri = mock()

        val usuarioMock = Usuario(
            mail = "test@example.com",
            nombreUsuario = "testuser",
            contrasena = "password",
            favoritos = emptyList(),
            imagenUri = fakeImageUri
        )

        whenever(mockUserRepository.obtenerUsuario()).thenReturn(usuarioMock)

        val loadInitialUserMethod = PerfilViewModel::class.java.getDeclaredMethod("loadInitialUser")
        loadInitialUserMethod.isAccessible = true
        loadInitialUserMethod.invoke(viewModel)

        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.first()

        verify(mockUserRepository).obtenerUsuario()

        assertEquals(fakeImageUri, currentState.imagenUri)
    }

    @Test
    fun `test setImage - actualiza el StateFlow y llama al repositorio`() = runTest {
        val newImageUri: Uri = mock()

        viewModel.setImage(newImageUri)

        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.first()

        assertEquals(newImageUri, currentState.imagenUri)

        verify(mockUserRepository).actualizarImagenPerfil(newImageUri)
    }

    @Test
    fun `test setImage con URI nula - elimina la imagen del StateFlow`() = runTest {
        viewModel.setImage(null)

        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.first()

        assertEquals(null, currentState.imagenUri)

        verify(mockUserRepository).actualizarImagenPerfil(null)
    }
}
