package com.example.progettoeafrontend.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val nomeProdotto: String,
    val prezzo: Double,
    val venditoreId:Long,
    val images : List<Image>

    )