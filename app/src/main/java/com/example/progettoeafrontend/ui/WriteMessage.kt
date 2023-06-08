package com.example.progettoeafrontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.progettoeafrontend.ScreenApp
import com.example.progettoeafrontend.UiStateSendMessage
import com.example.progettoeafrontend.viewModelMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteMessage(
    viewModelMessage:viewModelMessage,
    navController: NavController,
    venditoreId: Long,
    venditoreNome: String
) {
    var messageText by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }


    when(viewModelMessage.uiStateSendMessage ){
        is UiStateSendMessage.Loading -> {}
        is UiStateSendMessage.Success -> alert(navController = navController,viewModelMessage)
        is UiStateSendMessage.Error -> alert2(viewModelMessage)
    }



    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "venditore id ${venditoreId} venditoreNome: ${venditoreNome}")
        Text(
            text = "Scrivi un messaggio",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.focusRequester(focusRequester)
        )

        Button(
            onClick = {
                val message = messageText.text
                if (message.isNotBlank()) {
                    viewModelMessage.sendMessage(message,venditoreId,venditoreNome)
                    messageText = TextFieldValue()
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Invia messaggio")
        }
    }

//    focus su textField
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
private fun alert(
    navController: NavController,
    viewModelMessage: viewModelMessage

) {
    AlertDialog(
        onDismissRequest = {},
        title = {Text(text = "Conferma")},
        text = {Text(text = "Messaggio Inviato")},
        confirmButton = {
            TextButton(onClick = {goToHome(navController,viewModelMessage)}) {
                Text(text = "Torna alla Home")
            }
        }
    )
}

fun goToHome(navController: NavController, viewModelMessage: viewModelMessage) {
        viewModelMessage.reset()
        navController.navigate(ScreenApp.Home.name)
}

@Composable
private fun alert2(viewModelMessage: viewModelMessage) {
    AlertDialog(
        onDismissRequest = {},
        title = {Text(text = "Errore")},
        text = {Text(text = "Messaggio non Inviato")},
        confirmButton = {
            TextButton(onClick = {viewModelMessage.reset()}) {
                Text(text = "Riprova")
            }
        }
    )
}