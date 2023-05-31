package com.example.progettoeafrontend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiState
import com.example.progettoeafrontend.network.Image
import com.example.progettoeafrontend.network.Utente

@Composable
fun Account(uiState:UiState,
            modifier: Modifier = Modifier){
    Text(text = "Account sono io")



        when(uiState){
            is UiState.Loading -> LoadingScreen(modifier)
            is UiState.Success -> ResultScreen(uiState.utenti, modifier)
            is UiState.Error -> ErrorScreen(modifier)
        }

    }

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
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

//@Composable
//fun ResultScreen(uiState: List<Utente>, modifier: Modifier = Modifier) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier.fillMaxSize()
//    ) {
//        LazyColumn() {
//            items(uiState) { u ->
//                Text(text = u.id.toString())
//                Text(text = u.nome)
//                Text(text = u.cognome)
//
//            }
//        }
//    }
//
//}
@Composable
fun ResultScreen(uiState: List<Image>, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn() {
            items(uiState) { u ->
                Text(text = u.image)


            }
        }
    }

}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.loading_failed))
    }
}





