package com.example.progettoeafrontend.model

import kotlinx.serialization.Serializable


//usare stessi nomi var del backend dto
@Serializable
data class User(
    val id: Long,
    val nome: String,
    val cognome: String,
    val email: String,
    val password: String

    ) {
}