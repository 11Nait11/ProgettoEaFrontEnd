package com.example.progettoeafrontend.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Long,
    val nome: String,
    val cognome: String,
    val email: String,
    val password: String

    ) {
}