package com.example.progettoeafrontend.ui


import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.progettoeafrontend.viewModelProduct
import com.example.progettoeafrontend.R

import com.example.progettoeafrontend.UiStateProduct
import com.example.progettoeafrontend.ScreenApp
import com.example.progettoeafrontend.model.Product


/**Visualizza la lista di prodotti (se tutto è andato bene)*/
@Composable
fun Home(uiState:UiStateProduct, navController : NavController,
         viewModel: viewModelProduct,
         modifier: Modifier = Modifier, )
{

    if(uiState==UiStateProduct.Loading || uiState==UiStateProduct.Error) // todo: or lista vuota
        viewModel.getProducts()

    when(uiState){
        is UiStateProduct.Loading -> LoadingScreen(modifier)
        is UiStateProduct.Success -> ResultScreen(uiState.resultList as List<Product>, modifier,navController, viewModel )
        is UiStateProduct.Error -> ErrorScreen(modifier,viewModel)

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
fun ResultScreen(products: List<Product>, modifier: Modifier = Modifier, navController : NavController,viewModel: viewModelProduct) {
    Column() {

        Text(text = stringResource(id = R.string.articoliVendita), fontSize = 20.sp)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(items = products, key = { product -> product.id }) { product ->
                Box(
                    modifier = modifier.clickable { clickProduct(navController,viewModel,product)})
                {
                    photoCard(product = product)
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier,viewModel: viewModelProduct) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(stringResource(R.string.loading_failed))
            IconButton(onClick = { viewModel.refresh() }) {
                Icon(Icons.Default.Refresh, contentDescription = stringResource(id = R.string.refresh))
            }
        }
    }
}

/**formattazione Prodotto*/
@Composable
fun photoCard(product: Product, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally)
        {

            /**immagine prodotto - base64->ByteArray*/
            Box(modifier = Modifier.size(120.dp)) {
                val imageData: ByteArray = Base64.decode(product.images[0].image, Base64.DEFAULT)
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

                Text(text = product.nomeProdotto, style = MaterialTheme.typography.bodyMedium)

            /**info prodotto*/
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(4.dp)) // Spazio tra l'icona e il testo
                Text(
                    text = product.prezzo.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.Bottom),
                    painter = painterResource(id = R.drawable.euro),
                    contentDescription = stringResource(id = R.string.euro)
                )
            }
        }
    }
}

/**naviga su productDetail- disattivata getById*/
fun clickProduct(navController : NavController, viewModel: viewModelProduct, product: Product){
//    viewModel.getProductDetail(prodottoId = product.id)
    viewModel.setUiStateProductDetail(product)
    navController.navigate(ScreenApp.ProductDetail.name)

}


