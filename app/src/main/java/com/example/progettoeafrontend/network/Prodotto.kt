package com.example.progettoeafrontend.network

import kotlinx.serialization.Serializable

@Serializable
data class Prodotto(
    val id: Int,
    val nome: String,
    val cognome: String ) {
}