package com.example.progettoeafrontend.ui

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.progettoeafrontend.AppViewModel
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateProd
import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.screenApp


@Composable
fun ProductDetail(uiState: UiStateProd, navController : NavController,
                  viewModel: AppViewModel,
                  modifier: Modifier = Modifier,

                  ){

   Log.d("pippo","chiamo con stato in Product : ${uiState.toString()}")


    when(uiState){
        is UiStateProd.Loading -> LoadingScreen(modifier)
        is UiStateProd.Success -> ResultScreenProduct(uiState.result as Product, modifier,navController, viewModel )
        is UiStateProd.Error -> ErrorScreen(modifier)
    }

}

@Composable
fun ResultScreenProduct(product : Product, modifier: Modifier = Modifier, navController : NavController,viewModel: AppViewModel) {

    LazyColumn(){
    item {
        Text(text = "id" + product.id)
        Text(text = "nome Prodotto" + product.nomeProdotto)
        Text(text = "prezzo" + product.prezzo)
        Text(text = "vendiutore" + product.venditoreId)
    }
        items(items=product.images, key = { photo64 -> photo64.id }) { photo64 ->
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
    }



}