package com.example.baseproject.model

import android.net.Uri

data class Usuario(
    val mail: String,
    val nombreUsuario: String,
    val contrasena: String,
    val createdAT: Long = System.currentTimeMillis(),
    val favoritos: List<String>,
    val imagenUri: Uri? = null) {
}