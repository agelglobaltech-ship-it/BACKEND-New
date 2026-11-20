package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val idUsuario: Int? = null,
    val nombre: String,
    val correo: String,
    val password: String? = null,
    val rol: String = "estudiante"
)

@Serializable
data class LoginRequest(
    val correo: String,
    val password: String
)

@Serializable
data class Edificio(
    val idEdificio: Int? = null,
    val nombre: String,
    val descripcion: String? = null
)

@Serializable
data class Espacio(
    val idEspacio: Int? = null,
    val idEdificio: Int,
    val codigo: String
)

@Serializable
data class Reservacion(
    val idReservacion: Int? = null,
    val idUsuario: Int,
    val idEspacio: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val motivo: String? = null,
    val estado: String? = "pendiente"
)