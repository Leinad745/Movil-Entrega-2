package com.example.baseproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.data.UserRepository
import androidx.lifecycle.LiveData
import com.example.baseproject.model.Usuario

class RegViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    private val _autenticado = MutableLiveData<Boolean>()
    val autenticado: LiveData<Boolean> = _autenticado

    private val _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual: LiveData<Usuario?> = _usuarioActual

    private val _errorLogin = MutableLiveData<String?>()
    val errorLogin: LiveData<String?> = _errorLogin

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    /*----------------------*/

    fun usuarioExiste(): Boolean {
        return userRepository.usuarioGuardado()
    }

    fun registroUsuario(nombreUsuario: String, email: String, contrasena: String): Boolean {
        return if (nombreUsuario.isNotBlank() && email.isNotBlank() && contrasena.isNotBlank()) {
            val nuevoUsuario = Usuario(email, nombreUsuario, contrasena, favoritos = emptyList())
            userRepository.guardarUsuario(nuevoUsuario)
            _usuarioActual.value = nuevoUsuario
            _autenticado.value = true
            _registroExitoso.value = true
            true
        } else {
            _errorLogin.value = "Por favor, complete todos los campos."
            _registroExitoso.value = false
            false
        }
    }

    fun login(contrasena: String): Boolean {
        return if (userRepository.verificarContrasena(contrasena)) {
            _usuarioActual.value = userRepository.obtenerUsuario()
            _autenticado.value = true
            _errorLogin.value = null
            true
        } else {
            _errorLogin.value = null
            false
        }
    }

    fun loginCredenciales(username: String, password: String) : Boolean {
        val usuario = userRepository.obtenerUsuario()
        return if (usuario != null && usuario.nombreUsuario == username && usuario.contrasena == password) {
            _usuarioActual.value = usuario
            _autenticado.value = true
            _errorLogin.value = null
            true
        } else {
            _errorLogin.value = "Nombre de usuario o contraseña incorrectos."
            false
        }
    }

    fun logout() {
        _autenticado.value = false
        _usuarioActual.value = null
    }

    fun limpiarError() {
        _errorLogin.value = null
    }

    fun refrescarUsuario() {
        _usuarioActual.value = userRepository.obtenerUsuario()
    }

    fun borrarUsuario() {
        userRepository.borrarUsuario()
        _usuarioActual.value = null
        _autenticado.value = false
    }

    init {
        // VERIFICA EL USUARIO AL INICIAR EL VIEWMODEL
        val user = userRepository.obtenerUsuario()
        _usuarioActual.value = user
        _autenticado.value = false
    }

    //Actualizar facvoritos de usuario
    fun updateUser() {
        _usuarioActual.value = userRepository.obtenerUsuario()
    }
}