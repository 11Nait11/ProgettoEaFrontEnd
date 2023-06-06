package com.example.progettoeafrontend.ui

import android.app.Activity
import android.util.Base64
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.progettoeafrontend.AppViewModel
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateProductDetail
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.ScreenApp


@Composable
fun ProductDetail(uiState: UiStateProductDetail, navController : NavController, viewModel: AppViewModel, modifier: Modifier = Modifier)
{
    when(uiState){
        is UiStateProductDetail.Loading -> LoadingScreen(modifier)
        is UiStateProductDetail.Success -> ResultScreenProduct(uiState.result as Product, modifier,navController, viewModel )
        is UiStateProductDetail.Error -> ErrorScreen(modifier)
    }
}

@Composable
fun ResultScreenProduct(product : Product, modifier: Modifier = Modifier, navController : NavController,viewModel: AppViewModel) {

    var comprato by remember { mutableStateOf(false) }

    if(comprato){
        alert(
            product=product,
            backHome = { navController.navigate(ScreenApp.Home.name) },
            viewModel=viewModel
        )
//        comprato=false
    }

    LazyColumn(){
    item {
        Text(text = "id" + product.id)
        Text(text = "nome Prodotto" + product.nomeProdotto)
        Text(text = "prezzo" + product.prezzo)
        Text(text = "vendiutore" + product.venditoreId)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {comprato=true} ) {

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
private fun alert(
    product: Product,
    backHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
) {

    val activity = (LocalContext.current as Activity)
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val paga = remember { mutableStateOf("") }


    LaunchedEffect(key1 = product.prezzo) {
        try {
            paga.value=PaymentFactory.getImp().paga( product.prezzo)
            viewModel.deleteProduct(product.id)
        } catch (e: PaymentFailException) { errorMessage.value = "${e.message}" }
    }
    AlertDialog(
        onDismissRequest = {},
        title = {
            if (errorMessage.value != null)
                Text(text = stringResource(id = R.string.pagamentoKO))
            else
                Text(text = stringResource(id = R.string.pagamentoOK), textAlign = TextAlign.Center)
        },
        text = {
            if (errorMessage.value != null)
                Text(
                    text = errorMessage.value!!,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible)
             else
                    Text(
                        text = paga.value,
                        textAlign = TextAlign.Center, overflow = TextOverflow.Visible)
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = backHome) {
                Text(text = "Esci")
            }
        },
        confirmButton = {
            TextButton(onClick = backHome) {
                Text(text = "Torna alla Home")
            }
        }
    )
}




