package com.example.baseproject.data

import android.content.Context
import com.example.baseproject.model.Usuario
import android.content.SharedPreferences
import androidx.core.content.edit
import android.net.Uri
import androidx.core.net.toUri

class UserRepository(context: Context) {
    private val preferencias: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MAIL = "key_mail"
        private const val KEY_NOMBRE_USUARIO = "key_nombre_usuario"
        private const val KEY_CONTRASENA = "key_contrasena"
        private const val KEY_CREATED_AT = "key_created_at"
        private const val KEY_HAS_USER = "has_user"
        private const val KEY_FAVORITOS = "key_favoritos"
        private const val KEY_IMAGEN_URI = "key_imagen_uri"

    }

    fun guardarUsuario(usuario: Usuario) {
        preferencias.edit().apply {
            putString(KEY_MAIL, usuario.mail)
            putString(KEY_NOMBRE_USUARIO, usuario.nombreUsuario)
            putString(KEY_CONTRASENA, usuario.contrasena)
            putString(KEY_FAVORITOS, usuario.favoritos.joinToString(","))
            putLong(KEY_CREATED_AT, usuario.createdAT)
            putBoolean(KEY_HAS_USER, true)
            putString(KEY_IMAGEN_URI, usuario.imagenUri?.toString())
            apply()
        }
    }

    fun usuarioGuardado(): Boolean {
        return preferencias.getBoolean(KEY_HAS_USER, false)
    }

    fun obtenerUsuario(): Usuario? {
        if (!usuarioGuardado()) return null

        val username = preferencias.getString(KEY_NOMBRE_USUARIO, "") ?: ""
        val mail = preferencias.getString(KEY_MAIL, "") ?: ""
        val contrasena = preferencias.getString(KEY_CONTRASENA, "") ?: ""
        val createdAt = preferencias.getLong(KEY_CREATED_AT, 0L)

        val favoritosStr = preferencias.getString(KEY_FAVORITOS, "") ?: ""
        val favoritos = if (favoritosStr.isNotEmpty()) favoritosStr.split(",") else emptyList()
        val imagenUriString = preferencias.getString(KEY_IMAGEN_URI, null)
        val imagenUri = imagenUriString?.toUri()


        return if (username.isNotEmpty() && contrasena.isNotEmpty()) {
            Usuario(mail, username, contrasena, createdAt, favoritos, imagenUri)
        } else null
    }

    fun borrarUsuario() {
        preferencias.edit { clear() }
    }

    fun verificarContrasena(contraInput: String): Boolean {
        val contrasenaGuardada = preferencias.getString(KEY_CONTRASENA, "")
        return contrasenaGuardada == contraInput
    }

    fun actualizarImagenPerfil(uri: Uri?) {

        val usuarioActual = obtenerUsuario()

        if (usuarioActual != null) {
            val usuarioActualizado = usuarioActual.copy(imagenUri = uri)
            guardarUsuario(usuarioActualizado)
        }
    }
}