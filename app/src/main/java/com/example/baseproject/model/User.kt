package com.example.baseproject.model

data class Usuario(
    val mail: String,
    val nombreUsuario: String,
    val contrasena: String,
    val createdAT: Long = System.currentTimeMillis()) {
}