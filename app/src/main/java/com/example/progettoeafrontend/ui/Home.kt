package com.example.progettoeafrontend.ui

import android.util.Base64
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiState
import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.screenApp
import com.example.progettoeafrontend.ui.theme.ProgettoEaFrontEndTheme

@Composable
fun Home(uiState:UiState,
         modifier: Modifier = Modifier){

    when(uiState){
        is UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> ResultScreen(uiState.resultList as List<Image>, modifier)
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

@Composable
fun ResultScreen(uiState: List<Image>, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn() {
            items(uiState) { image ->
                photoCard(photo64 = image.image)
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


@Composable
fun photoCard(photo64: String, modifier: Modifier = Modifier) {

    val imageData: ByteArray = Base64.decode(photo64, Base64.DEFAULT)
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageData)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(id = R.string.img_prodotto) ,
        contentScale = ContentScale.FillBounds
    )
    Text(text = "--------------------------------------------------------------------")
}

