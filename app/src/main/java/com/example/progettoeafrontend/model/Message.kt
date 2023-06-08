package com.example.progettoeafrontend.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
@Serializable
data class Message(var id: Long=0,
              var testo: String,
              var dataInvio: String="",
              var mittenteNome: String="",
              var destinatarioNome: String="",
              var mittenteId: Long,
              var destinatarioId: Long) {
}