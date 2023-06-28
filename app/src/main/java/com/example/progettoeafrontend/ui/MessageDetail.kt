package com.example.progettoeafrontend.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.network.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


/**scorre lista della conversazione tra due utenti */
@Composable//tutti i messaggi dell'utente loggato
fun MessageDetail(value: List<Message>?, navController: NavHostController) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.dettagliMessaggio),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            /**scorre lista*/
            if (!value.isNullOrEmpty()) {
                LazyColumn {
                    items(value) { item ->
                        GestioneMessaggi(item)
                    }
                }

                /**scrivi messaggio - una volta scritto messaggio ritorno alla home*/
                //TODO:ritornare nella lista messaggi? cambia fun alertSuccess() in WriteMessage
                IconButton( // button per scrivere nuovo mess (nella conversazione)
                    onClick =
                    {
                        if(value[0].mittenteId== Service.accessId) //per capire il destinatario
                            goToWriteMessage(navController,value[0].destinatarioId,value[0].destinatarioNome)
                        else
                            goToWriteMessage(navController,value[0].mittenteId,value[0].mittenteNome)
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(start = 8.dp, bottom = 8.dp)
                        .size(54.dp)
                ) { Icon(painterResource(id = R.drawable.add_message__100), contentDescription = "Aggiungi") }
            } //chiude if
            else
                Text(text = stringResource(id = R.string.listaMessaggiVuota))

        }
    }
}

/**formatta messaggio:nomeMittente-dataInvio-TestoMessaggio*/
@Composable
fun GestioneMessaggi(item: Message) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(4.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.Start),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**nome mittente + dataInvio */
                Text(
                    text = item.mittenteNome,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 2.dp)
                )
                Text(
                    text = formatTime(item.dataInvio),
//                    text = item.dataInvio,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 2.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, Color.Black)
                    .background(Color(0xFFF5F5F5)) //bianco sporco
                    .align(Alignment.CenterHorizontally)
            ) {
                /**Testo messaggio */
                Text(
                    text = item.testo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(8.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


/**formatta LocalDateTime*/
private fun formatTime(time: String): String {
    val inputFormats = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS")
    )
    val outputFormat = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")
    var dateTime: LocalDateTime? = null
    for (format in inputFormats) {
        try {
            dateTime = LocalDateTime.parse(time, format)
            break
        } catch (e: DateTimeParseException) {

        }
    }
    if (dateTime != null) {
        return outputFormat.format(dateTime)
    }
    // Restituisci una stringa vuota o un valore di default se non è possibile analizzare la data
    return ""
}