package com.example.tutorialcompose2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrazione() {
    val nome = remember { mutableStateOf("") }
    val cognome = remember { mutableStateOf("") }
    val mail = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val telefono = remember { mutableStateOf("") }
    val codiceFiscale = remember { mutableStateOf("") }
    val indirizzo = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Nome:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = nome.value,
                    onValueChange = { nome.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Divider(
                    modifier = Modifier.height(5.dp),
                    color = Color(0xFF007782)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Cognome:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = cognome.value,
                    onValueChange = { cognome.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Divider(
                    modifier = Modifier.height(5.dp),
                    color = Color(0xFF007782)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Mail:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = mail.value,
                    onValueChange = { mail.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Divider(
                    modifier = Modifier.height(5.dp),
                    color = Color(0xFF007782)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Password:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))
                Divider(modifier = Modifier.height(5.dp), color = Color(0xFF007782))
                Spacer(modifier = Modifier.height(10.dp))


            }

            Column {
                Button(
                    onClick = {
                        // Creo un oggetto utente con i dati inseriti nei textfield
                        val utente = Utente(nome, cognome, mail, password, telefono, codiceFiscale, indirizzo)

                        // Implementare codice per inserire l'utente nel database -----------------------------------

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007782))
                ) {
                    Text(text = "Registrati")
                }

            }
        }
    }
}

data class Utente(
    val nome: MutableState<String>,
    val cognome: MutableState<String>,
    val mail: MutableState<String>,
    val password: MutableState<String>,
    val telefono: MutableState<String>,
    val codiceFiscale: MutableState<String>,
    val indirizzo: MutableState<String>
)

@Preview
@Composable
fun RegistrazionePreview() {
    Registrazione()
}
