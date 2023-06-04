package com.example.progettoeafrontend.ui

import android.app.Activity
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.progettoeafrontend.AppViewModel
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateProd
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.ScreenApp


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
    var comprato by remember { mutableStateOf(false) }

    if(comprato)
        clickCompra(product.prezzo,{ navController.navigate(ScreenApp.Home.name) })


    val handleCompraClick: (Long) -> Unit = {   id->
        comprato = true
        viewModel.deleteProductFromDb(id)
    }

    LazyColumn(){
    item {
        Text(text = "id" + product.id)
        Text(text = "nome Prodotto" + product.nomeProdotto)
        Text(text = "prezzo" + product.prezzo)
        Text(text = "vendiutore" + product.venditoreId)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {handleCompraClick(product.id)} ) {

                    Icon(
                        painterResource(id = R.drawable.pay),
                        contentDescription = stringResource(id = R.string.cart),
                        modifier = Modifier.size(24.dp) // Modifica la dimensione dell'icona se necessario
                    )
                    Text(
                        text=stringResource(id = R.string.acquista),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.padding(start = 8.dp)
                    )
            }

        }

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
@Composable
private fun clickCompra(
    prezzo: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val activity = (LocalContext.current as Activity)

    val errorMessage = remember { mutableStateOf<String?>(null) }
    val paga = remember { mutableStateOf("") }

    LaunchedEffect(key1 = prezzo) {
        try {
            paga.value=PaymentFactory.getImp().paga(prezzo)
        } catch (e: PaymentFailException) {
            errorMessage.value = "${e.message}"
        }
    }

    AlertDialog(
        onDismissRequest = {},
        title = {
            if (errorMessage.value != null) {
                Text(
                    text = stringResource(id = R.string.pagamentoKO)
                )
            } else {
                Text(text = stringResource(id = R.string.pagamentoOK), textAlign = TextAlign.Center)
            }
        },
        text = {
            if (errorMessage.value != null) {
                Text(
                    text = errorMessage.value!!,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                )
            } else {

                    Text(
                        text = paga.value,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Visible,
                    )

            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onClick) {
                Text(text = "Esci")
            }
        },
        confirmButton = {
            TextButton(onClick = onClick) {
                Text(text = "Torna alla Home")
            }
        }
    )
}




