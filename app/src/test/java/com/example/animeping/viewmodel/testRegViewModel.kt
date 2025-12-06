package com.example.animeping.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.animeping.data.UserRepository
import com.example.animeping.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TestRegViewModel {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockApplication: Application
    private lateinit var mockUserRepository: UserRepository
    private lateinit var viewModel: RegViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockApplication = mock()
        mockUserRepository = mock()

        viewModel = mock(defaultAnswer = Answers.CALLS_REAL_METHODS)
        val userRepoField = RegViewModel::class.java.getDeclaredField("userRepository")
        userRepoField.isAccessible = true
        userRepoField.set(viewModel, mockUserRepository)

        val autenticadoField = RegViewModel::class.java.getDeclaredField("_autenticado")
        autenticadoField.isAccessible = true

        val mutableAutenticado = MutableLiveData<Boolean>()
        autenticadoField.set(viewModel, mutableAutenticado)
        whenever(viewModel.autenticado).thenReturn(mutableAutenticado) // Hacemos que el getter público devuelva nuestro LiveData

        val usuarioActualField = RegViewModel::class.java.getDeclaredField("_usuarioActual")
        usuarioActualField.isAccessible = true

        val mutableUsuarioActual = MutableLiveData<Usuario?>()
        usuarioActualField.set(viewModel, mutableUsuarioActual)
        whenever(viewModel.usuarioActual).thenReturn(mutableUsuarioActual)

        val errorLoginField = RegViewModel::class.java.getDeclaredField("_errorLogin")
        errorLoginField.isAccessible = true

        val mutableErrorLogin = MutableLiveData<String?>()
        errorLoginField.set(viewModel, mutableErrorLogin)
        whenever(viewModel.errorLogin).thenReturn(mutableErrorLogin)

        val registroExitosoField = RegViewModel::class.java.getDeclaredField("_registroExitoso")
        registroExitosoField.isAccessible = true

        val mutableRegistroExitoso = MutableLiveData<Boolean>()
        registroExitosoField.set(viewModel, mutableRegistroExitoso)
        whenever(viewModel.registroExitoso).thenReturn(mutableRegistroExitoso)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test registroUsuario exitoso actualiza LiveData correctamente`() = runTest {
        val username = "testuser"
        val email = "test@example.com"
        val password = "password123"

        // Act
        val resultado = viewModel.registroUsuario(username, email, password)

        // Assert
        assertTrue("El método de registro debería devolver true", resultado)
        verify(mockUserRepository).guardarUsuario(any())
        assertEquals(true, viewModel.registroExitoso.value)
        assertEquals(true, viewModel.autenticado.value)
        assertEquals(username, viewModel.usuarioActual.value?.nombreUsuario)
        assertEquals(email, viewModel.usuarioActual.value?.mail)
        assertNull(viewModel.errorLogin.value)
    }

    @Test
    fun `test registroUsuario con campos vacios falla y actualiza error`() = runTest {
        val resultado = viewModel.registroUsuario("", "test@example.com", "password")

        assertFalse(resultado)
        assertEquals(false, viewModel.registroExitoso.value)
        assertEquals("Por favor, complete todos los campos.", viewModel.errorLogin.value)
        assertNotEquals(true, viewModel.autenticado.value)
    }

    @Test
    fun `test login con credenciales correctas`() = runTest {
        val username = "testuser"
        val password = "password123"
        val usuarioMock = Usuario("test@example.com", username, password, favoritos = emptyList())

        whenever(mockUserRepository.obtenerUsuario()).thenReturn(usuarioMock)

        val resultado = viewModel.loginCredenciales(username, password)

        assertTrue(resultado)
        assertEquals(true, viewModel.autenticado.value)
        assertEquals(username, viewModel.usuarioActual.value?.nombreUsuario)
        assertNull(viewModel.errorLogin.value)
    }

    @Test
    fun `test login con credenciales incorrectas`() = runTest {
        val username = "testuser"
        val password = "wrongpassword"
        val usuarioMock = Usuario("test@example.com", username, "correctpassword", favoritos = emptyList())

        whenever(mockUserRepository.obtenerUsuario()).thenReturn(usuarioMock)

        val resultado = viewModel.loginCredenciales(username, password)

        assertFalse(resultado)
        assertNotEquals(true, viewModel.autenticado.value)
        assertEquals("Nombre de usuario o contraseña incorrectos.", viewModel.errorLogin.value)
    }

    @Test
    fun `test refrescarUsuario actualiza el usuario desde el repositorio`() = runTest {
        // Arrange: Define el usuario que el repositorio debería devolver
        // Corregido: El ID va como último parámetro nombrado
        val usuarioRefrescado = Usuario("new@example.com", "newuser", "newpass", 123, emptyList())
        whenever(mockUserRepository.obtenerUsuario()).thenReturn(usuarioRefrescado)

        // Act: Llama a la función para refrescar el usuario
        viewModel.refrescarUsuario()

        // Assert: Verifica que el LiveData del usuario actual se actualizó
        assertEquals(usuarioRefrescado, viewModel.usuarioActual.value)
    }

    @Test
    fun `test logout actualiza LiveData correctamente`() = runTest {
        // Arrange: Simula un estado de login previo usando reflexión para acceder a los campos privados
        val autenticadoField = RegViewModel::class.java.getDeclaredField("_autenticado")
        autenticadoField.isAccessible = true
        (autenticadoField.get(viewModel) as MutableLiveData<Boolean>).value = true

        val usuarioActualField = RegViewModel::class.java.getDeclaredField("_usuarioActual")
        usuarioActualField.isAccessible = true
        (usuarioActualField.get(viewModel) as MutableLiveData<Usuario?>).value = Usuario("test@example.com", "testuser", "password", 123, emptyList())

        // Act: Llama a la función de logout
        viewModel.logout()

        // Assert: Verifica que el estado se haya limpiado (ahora sí puedes leer el .value público)
        assertEquals(false, viewModel.autenticado.value)
        assertNull(viewModel.usuarioActual.value)
    }

    @Test
    fun `test limpiarError borra el mensaje de error`() = runTest {
        // Arrange: Simula un mensaje de error existente usando reflexión
        val errorLoginField = RegViewModel::class.java.getDeclaredField("_errorLogin")
        errorLoginField.isAccessible = true
        (errorLoginField.get(viewModel) as MutableLiveData<String?>).value = "Este es un error de prueba."

        // Act: Llama a la función para limpiar el error
        viewModel.limpiarError()

        // Assert: Verifica que el error se haya eliminado
        assertNull(viewModel.errorLogin.value)
    }

    @Test
    fun `test borrarUsuario elimina el usuario y resetea el estado`() = runTest {
        // Arrange: Simula un estado de login usando reflexión
        val autenticadoField = RegViewModel::class.java.getDeclaredField("_autenticado")
        autenticadoField.isAccessible = true
        (autenticadoField.get(viewModel) as MutableLiveData<Boolean>).value = true

        val usuarioActualField = RegViewModel::class.java.getDeclaredField("_usuarioActual")
        usuarioActualField.isAccessible = true
        (usuarioActualField.get(viewModel) as MutableLiveData<Usuario?>).value = Usuario("test@example.com", "testuser", "password", 123, emptyList())

        // Act: Llama a la función para borrar el usuario
        viewModel.borrarUsuario()

        // Assert: Verifica que el usuario fue borrado del repositorio y el estado reseteado
        verify(mockUserRepository).borrarUsuario()
        assertNull(viewModel.usuarioActual.value)
        assertEquals(false, viewModel.autenticado.value)
    }


}
