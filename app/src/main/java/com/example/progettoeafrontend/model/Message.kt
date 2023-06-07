package com.example.progettoeafrontend.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class Message(
    val id: Long,
    val testo: String,
    val dataInvio: String,
    val mittenteNome:String,
    val destinatarioNome:String,
    val mittenteId: Long,
    val destinatarioId: Long,

)
