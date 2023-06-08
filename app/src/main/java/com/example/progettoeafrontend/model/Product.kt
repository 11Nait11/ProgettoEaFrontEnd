package com.example.progettoeafrontend.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val nomeProdotto: String,
    val prezzo: Double,
    val venditoreId:Long,
    val venditoreNome:String,
    val images : List<Image>

    )