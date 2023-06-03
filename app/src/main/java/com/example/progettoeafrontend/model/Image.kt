package com.example.progettoeafrontend.model

import kotlinx.serialization.Serializable


@Serializable
data class Image(val id:Long,val image: String, val prodottoId:Long)
