package com.example.progettoeafrontend.ui


import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progettoeafrontend.R


@Composable
fun MessageDetail(value: List<String>?)
{
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Dettagli del messaggio", fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))

            // Verifica se la lista value non è nulla e non è vuota
            if (!value.isNullOrEmpty()) {
                LazyColumn {
                    items(value) { item ->
                        Text(text = item)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Text(text = "Nessun dettaglio del messaggio disponibile")
            }
        }
    }

}


@Composable
fun GestioneMessaggi(nome: String, descrizione: List<String>,  immagine: Int){
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Gestisci il click del pulsante indietro */ },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Indietro"
                    )
                }
                Image(
                    painter = painterResource(immagine),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .align(Alignment.CenterVertically),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = nome,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                )
            }

            Divider(modifier = Modifier.fillMaxWidth().height(5.dp), color = Color.Transparent)
            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                items(descrizione) { desc ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Black)
                        .background(Color(0xFFF5F5F5)) //bianco sporco
                        .align(Alignment.CenterHorizontally)
                    ){
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(1f)
                                .padding(8.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


        }
    }
}

@Preview
@Composable
fun Preview() {
    GestioneMessaggi(
        nome = "Massimo",
        descrizione = listOf("Descrizione1aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Descrizione2", "Descrizione3", "Descrizione4aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Descrizione5bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", "Descrizione6dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd", "Descrizione7", "Descrizione8", "Descrizione9", "Descrizione10", "Descrizione11asdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddwqdqwdeqf",),
        immagine = R.drawable.ic_broken_image
    )
}