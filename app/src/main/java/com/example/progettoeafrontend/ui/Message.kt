package com.example.progettoeafrontend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateMessage
import com.example.progettoeafrontend.UiStateProduct
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.viewModelMessage
import com.example.progettoeafrontend.viewModelProduct


@Composable
fun Message(uiState:UiStateMessage, viewModel: viewModelMessage,modifier: Modifier = Modifier){

    Text(text = "Benvenoob sono Message")

    if(uiState== UiStateMessage.Loading || uiState== UiStateMessage.Error) // todo: or lista vuota
        viewModel.getMessages()

    when(uiState){
        is UiStateMessage.Loading -> LoadingScreenMessage(modifier)
        is UiStateMessage.Success -> ResultScreenMessage(uiState.resultList as List<Message>)
        is UiStateMessage.Error -> ErrorScreenMessage(modifier)
    }

    
}

@Composable
fun ResultScreenMessage(messages:List<Message>) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {

            //Casella Messaggi
            Text(text = "Casella Messaggi ", fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(messages) { message ->
                    Row{
                        MessageItem(message)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { { } }
    ) {
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
//                todo if(mittente!=da loggato) text=mittente else destinatario
                text = message.destinatarioNome,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message.testo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ErrorScreenMessage(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.loading_failed))
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