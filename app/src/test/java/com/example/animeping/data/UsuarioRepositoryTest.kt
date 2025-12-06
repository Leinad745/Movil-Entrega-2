package com.example.animeping.data

import org.mockito.Mock
import android.content.Context
import android.content.SharedPreferences
import com.example.animeping.model.Usuario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.check
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import kotlin.test.assertNotNull

class UsuarioRepositoryTest {
    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockPrefs: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var userRepository: UserRepository

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        `when`(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE)))
            .thenReturn(mockPrefs)

        `when`(mockPrefs.edit()).thenReturn(mockEditor)

        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)
        `when`(mockEditor.putLong(anyString(), anyLong())).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean(anyString(), eq(true))).thenReturn(mockEditor)
        `when`(mockEditor.clear()).thenReturn(mockEditor)

        userRepository = UserRepository(mockContext)
    }

    @Test
    fun `guardarUsuario guarda los datos en las preferencias`() {
        val usuario = Usuario(
            mail = "test@test.com",
            nombreUsuario = "Hibari",
            contrasena = "contrasena123",
            createdAT = 100L,
            favoritos = listOf("anime1", "anime2"),
            imagenUri = null
        )
        
        userRepository.guardarUsuario(usuario)

        verify(mockEditor).putString("key_mail", "test@test.com")
        verify(mockEditor).putString("key_nombre_usuario", "Hibari")
        verify(mockEditor).putString("key_contrasena", "contrasena123")
        verify(mockEditor).putString("key_favoritos", "anime1,anime2")
        verify(mockEditor).putBoolean("has_user", true)
        verify(mockEditor).putLong("key_created_at", 100L)
        verify(mockEditor).putString("key_imagen_uri", null)
        verify(mockEditor).apply()
    }

    @Test
    fun `obtenerUsuario devuelve el usuario guardado`() {
        `when`(mockPrefs.getBoolean("has_user", false)).thenReturn(true)
        `when`(mockPrefs.getString(eq("key_mail"), anyString())).thenReturn("test@test.com")
        `when`(mockPrefs.getString(eq("key_nombre_usuario"), anyString())).thenReturn("Hibari")
        `when`(mockPrefs.getString(eq("key_contrasena"), anyString())).thenReturn("contrasena123")
        `when`(mockPrefs.getString(eq("key_favoritos"), anyString())).thenReturn("anime1,anime2")
        `when`(mockPrefs.getString(eq("key_imagen_uri"), anyString())).thenReturn(null)
        `when`(mockPrefs.getLong(eq("key_created_at"), anyLong())).thenReturn(100L)

        val usuario = userRepository.obtenerUsuario()

        assertNotNull(usuario)
        assertEquals("test@test.com", usuario.mail)
        assertEquals(2, usuario?.favoritos?.size)
        assertEquals("anime1", usuario?.favoritos?.get(0))
    }

    @Test
    fun `verificarContrasena devuelve true si la contraseña es correcta`(){
        `when`(mockPrefs.getString("key_contrasena", "")).thenReturn("contrasena123")

        val esCorrecta = userRepository.verificarContrasena("contrasena123")
        assertTrue(esCorrecta)
    }

    @Test
    fun `addFavorite agrega un anime a la lista de favoritos`(){
        `when`(mockPrefs.getBoolean("has_user", false)).thenReturn(true)
        `when`(mockPrefs.getString(eq("key_nombre_usuario"), anyString())).thenReturn("Hibari")
        `when`(mockPrefs.getString(eq("key_contrasena"), anyString())).thenReturn("contrasena123")
        `when`(mockPrefs.getString(eq("key_favoritos"), anyString())).thenReturn("1")

        userRepository.addFavorite("2")

        verify(mockEditor).putString(eq("key_favoritos"), check{
            assertTrue(it.contains("1"))
            assertTrue(it.contains("2"))
        })
        verify(mockEditor).apply()

    }
}