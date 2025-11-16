package com.example.animeping.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeping.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val imagenUri: Uri? = null
)

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: UserRepository = UserRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        loadInitialUser()
    }

    private fun loadInitialUser() {
        val user = repositorio.obtenerUsuario()
        _uiState.update { currentState ->
            currentState.copy(imagenUri = user?.imagenUri)
        }
    }

    fun setImage(uri: Uri?) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(imagenUri = uri)
            }
            repositorio.actualizarImagenPerfil(uri)
        }
    }

}