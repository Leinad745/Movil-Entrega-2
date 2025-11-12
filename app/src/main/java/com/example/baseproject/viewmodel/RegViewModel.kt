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

    fun registroUsuario(nombreUsuario: String, email: String, contrasena: String){
        return if (nombreUsuario.isNotBlank() && email.isNotBlank() && contrasena.isNotBlank()) {
            val nuevoUsuario = Usuario(email, nombreUsuario, contrasena, List)
        }
    }

    init {
        // VERIFICA EL USUARIO AL INICIAR EL VIEWMODEL
        val user = userRepository.obtenerUsuario()
        _usuarioActual.value = user
        _autenticado.value = false
    }
}