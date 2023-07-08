package com.example.progettoeafrontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.progettoeafrontend.R
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
        is UiStateSendMessage.Success -> alertSuccess(navController, viewModelMessage)
        is UiStateSendMessage.Error -> alertError(viewModelMessage)
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /** Scrivimi messaggio */
            Text(
                text = "Scrivi un Messaggio a:\n\n ${venditoreNome}",
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
            )

            /** Button invio messaggio al backEnd */
            Button(
                onClick = {
                    val message = messageText.text
                    if (message.isNotBlank()) {
                        viewModelMessage.sendMessage(message, venditoreId) //@post save message
                        if (viewModelMessage.uiStateSendMessage == UiStateSendMessage.Success) {
                            messageText = TextFieldValue()
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF007782))
            ) {
                Text(text = "Invia messaggio", color = Color.White)
            }
        }
    }



//    focus su textField
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }

}//fine fun writeMessage

/**ritorna alla home e resetta stato Message(cosi visualizza eventuali nuovi messaggi nella successiva richiesta)*/
fun goToHome(navController: NavController, viewModelMessage: viewModelMessage) {
    viewModelMessage.setLoadingSendMessageState()
    viewModelMessage.setLoadingMessageState()
    navController.navigate(ScreenApp.Message.name)
}

/**atterro sulla home se il messaggio è stato salvato correttamente*/
@Composable
private fun alertSuccess(navController: NavController = rememberNavController(), viewModelMessage: viewModelMessage)
{
    AlertDialog(
        onDismissRequest = {},
        title = {Text(text =stringResource(id = R.string.conferma))},
        text = {Text(text = stringResource(id = R.string.messaggioInviato))},
        confirmButton = {
            TextButton(onClick = { goToHome(navController,viewModelMessage) }) {
                Text(text = stringResource(id = R.string.backToHome))
            }
        }
    )
}

@Composable
private fun alertError(viewModelMessage: viewModelMessage)
{
    AlertDialog(
        onDismissRequest = {},
        title = {Text(text =stringResource(id = R.string.errore))},
        text = {Text(text = stringResource(id = R.string.messaggioNonInviato))},
        confirmButton = {
            TextButton(onClick = {viewModelMessage.setLoadingSendMessageState()}) {
                Text(text = stringResource(id = R.string.riprova))
            }
        }
    )
}