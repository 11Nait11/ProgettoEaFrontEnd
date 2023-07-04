package com.example.progettoeafrontend.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.ScreenApp
import com.example.progettoeafrontend.UiStateMessage
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.viewModelMessage
import com.google.gson.Gson


@Composable
fun MessageList(uiState:UiStateMessage, viewModel: viewModelMessage, navController: NavController, modifier: Modifier = Modifier){

    if(uiState== UiStateMessage.Loading || uiState== UiStateMessage.Error)
        viewModel.getMessages()

    when(uiState){
        is UiStateMessage.Loading -> LoadingScreenMessage(modifier)
        is UiStateMessage.Success -> ResultScreenMessage(uiState.map,navController)
        is UiStateMessage.Error -> ErrorScreenMessage(modifier,viewModel)
    }

}


/** Scorre elementi mappa e mostra anteprima*/
@Composable
fun ResultScreenMessage(
    conversazione: MutableMap<Pair<String, String>, MutableList<Message>>,
    navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Casella Messaggi ", fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(conversazione.entries.toList()) { entry ->
                    Row{
                        anteprimaMessaggi(entry.key,entry.value,navController) //chiave conversazione + intera conversazione
//                        stampa(value = entry.value)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}


/**anterpima clickabile, manda lista  messaggi al navController tramite json
 * utilizzare Uri.encode per leggere caratteri speciali - TODO:fix errore con alcuni caratteri speciali */
@Composable
fun anteprimaMessaggi(key: Pair<String, String>, value: MutableList<Message>, navController: NavController) {
    val jsonValue = Gson().toJson(value)
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { navController.navigate("${ScreenApp.MessageDetails.name}/${Uri.encode(jsonValue)}")})
    {
        Image(
            painter = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = key.second,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value[0].testo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}



@Composable
fun ErrorScreenMessage(modifier: Modifier = Modifier,viewModel: viewModelMessage) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(stringResource(R.string.failed))
            IconButton(onClick = { viewModel.setLoadingMessageState() }) {
                Icon(Icons.Default.Refresh, contentDescription = stringResource(id = R.string.refresh))
            }
        }
    }

}

@Composable
fun  LoadingScreenMessage(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )

    }
}