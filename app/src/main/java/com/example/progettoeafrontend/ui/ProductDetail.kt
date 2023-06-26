package com.example.progettoeafrontend.ui

import android.app.Activity
import android.util.Base64
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.progettoeafrontend.ViewModelProduct
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateProductDetail
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.ScreenApp


@Composable
fun ProductDetail(uiState: UiStateProductDetail, navController : NavController, viewModel: ViewModelProduct, modifier: Modifier = Modifier)
{
    when(uiState){
        is UiStateProductDetail.Loading -> LoadingScreen(modifier)
        is UiStateProductDetail.Success -> ResultScreenProduct(uiState.result as Product, modifier,navController, viewModel )
        is UiStateProductDetail.Error -> ErrorScreen(modifier,viewModel)
    }
}

@Composable
fun ResultScreenProduct(product : Product, modifier: Modifier = Modifier, navController : NavController,viewModel: ViewModelProduct) {

    if(viewModel.comprato){
        alert(
            product=product,
            backToHome = { backToHome(navController,viewModel) },
            viewModel=viewModel )
    }
    Carousel(product =product,viewModel,navController)
}



@Composable
fun Carousel(product: Product, viewModel: ViewModelProduct, navController: NavController) {
    Text(text = "Dettagli Annuncio", fontSize = 30.sp)

    /** Immagine Principale*/
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 40.dp)) {
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items=product.images, key = { photo64 -> photo64.id }) {
                    photo64 ->
                val imageData: ByteArray = Base64.decode(photo64.image, Base64.DEFAULT)

                Box(
                    modifier = Modifier.size(400.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageData) /**   byteArray da base64*/
                            .crossfade(true)
                            .build(),
                        error = painterResource(R.drawable.ic_broken_image),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = stringResource(id = R.string.img_prodotto),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }


        /** button ContattaVenditore + Compra */
        Spacer(modifier = Modifier.height(16.dp))
        Row() {

            /** Contatta */
            Button(
                onClick = { goToWriteMessage(navController,product.venditoreId,product.venditoreNome) }) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = stringResource(id = R.string.sendMex),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = stringResource(id = R.string.sendMex),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            /** Compra */
            Button(
                onClick = { viewModel.setCompratoTrue() }) {
                Icon(
                    painterResource(id = R.drawable.pay),
                    contentDescription = stringResource(id = R.string.cart),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = stringResource(id = R.string.acquista),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**  Varie info */
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp), color = Color(0xFF007782)
                )

                //Costi Spedizione
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Spedizione: ", fontSize = 20.sp, modifier = Modifier.padding(start = 5.dp))
                Text(text = "Tutte le spedizioni sono gratuite nel nostro sito", modifier = Modifier.padding(start = 5.dp))
                Spacer(modifier = Modifier.height(5.dp))

                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp), color = Color(0xFF007782)
                )

                //Desrizione Articolo
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Descrizione: ", fontSize = 20.sp, modifier = Modifier.padding(start = 5.dp))
                Text(text = "descriptions", modifier = Modifier.padding(start = 5.dp))
                Spacer(modifier = Modifier.height(5.dp))


                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp), color = Color(0xFF007782)
                )

                //Prezzo del Prodotto
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Prezzo: ", fontSize = 20.sp, modifier = Modifier.padding(start = 5.dp))
                Text(text = "${product.prezzo} €", modifier = Modifier.padding(start = 5.dp))
                Spacer(modifier = Modifier.height(5.dp))

                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp), color = Color(0xFF007782)
                )

                //Recensione più vista
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Recensione: ", fontSize = 20.sp, modifier = Modifier.padding(start = 5.dp))
                Text(text = "reviews", modifier = Modifier.padding(start = 5.dp))
                Spacer(modifier = Modifier.height(5.dp))

                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp), color = Color(0xFF007782)
                )
            }

        }
    }

}
//Todo serve venditoreNome??
fun goToWriteMessage(navController: NavController, venditoreId: Long, venditoreNome: String) {
    navController.navigate(ScreenApp.WriteMessage.name + "?venditoreId=$venditoreId&venditoreNome=$venditoreNome")
}

fun backToHome(navController: NavController, viewModel: ViewModelProduct) {
    navController.navigate(ScreenApp.Home.name)
    viewModel.setCompratoFalse()
}

@Composable
private fun alert(
    product: Product,
    backToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewModelProduct
) {

    val activity = (LocalContext.current as Activity)
    val pagamentoSceltoError = remember { mutableStateOf<String?>(null) }
    val pagamentoScelto = remember { mutableStateOf("") }


    /**Ritorna il pagamento scelto o excpetion */
    LaunchedEffect(key1 = product.prezzo) {
        try {
            pagamentoScelto.value=PaymentFactory.getImp().paga( product.prezzo) //ritorna una stringa
            viewModel.deleteProduct(product.id)
        }
        catch (e: PaymentFailException) {
            pagamentoSceltoError.value = "${e.message}"
        }
    }

    /**visualizza Messaggio di avvenuto pagamento o di errore */
    AlertDialog(
        onDismissRequest = {},
        title = {
            if (pagamentoSceltoError.value != null)
                Text(text = stringResource(id = R.string.pagamentoKO))
            else
                Text(text = stringResource(id = R.string.pagamentoOK), textAlign = TextAlign.Center)
        },
        text = {
            if (pagamentoSceltoError.value != null)
                Text(
                    text = pagamentoSceltoError.value!!,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible)
            else
                Text(
                    text = pagamentoScelto.value,
                    textAlign = TextAlign.Center, overflow = TextOverflow.Visible)
        },
        modifier = modifier,

        /**button di uscita*/
        dismissButton = {
            TextButton(onClick = backToHome) {
                Text(text = "Esci")
            }
        },
        confirmButton = {
            TextButton(onClick = backToHome) {
                Text(text = "Torna alla Home")
            }
        }
    )
}


