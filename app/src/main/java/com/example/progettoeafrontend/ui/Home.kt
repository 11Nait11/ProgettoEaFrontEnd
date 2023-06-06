package com.example.progettoeafrontend.ui


import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.progettoeafrontend.AppViewModel
import com.example.progettoeafrontend.R

import com.example.progettoeafrontend.UiStateProduct
import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.ScreenApp


@Composable
fun Home(uiState:UiStateProduct, navController : NavController,
         viewModel: AppViewModel,
         modifier: Modifier = Modifier,

         ){

    if(uiState==UiStateProduct.Loading) // todo: or lista vuota
        viewModel.getImages()
    when(uiState){
        is UiStateProduct.Loading -> LoadingScreen(modifier)
        is UiStateProduct.Success -> ResultScreen(uiState.resultList as List<Image>, modifier,navController, viewModel )
        is UiStateProduct.Error -> ErrorScreen(modifier)
    }

}

@Composable
fun  LoadingScreen(modifier: Modifier = Modifier) {
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
fun ResultScreen(photos: List<Image>, modifier: Modifier = Modifier, navController : NavController,viewModel: AppViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = photos, key = { photo -> photo.id }) { photo ->
            Box(
                modifier = modifier.clickable { clickProduct(navController, viewModel, photo.prodottoId) })
            {
                photoCard(photo64 = photo)
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
fun photoCard(photo64: Image, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        val imageData: ByteArray = Base64.decode(photo64.image, Base64.DEFAULT)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageData)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(id = R.string.img_prodotto),
            contentScale = ContentScale.FillBounds
        )
    }
    Text(text =photo64.id.toString() )
    Text(text =photo64.id.toString() )
}

fun clickProduct(navController : NavController,viewModel: AppViewModel,id:Long){
    viewModel.getProductDetail(prodottoId = id)
    navController.navigate(ScreenApp.ProductDetail.name)

}
