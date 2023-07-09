package com.example.progettoeafrontend.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.progettoeafrontend.UiStateProductAdd
import com.example.progettoeafrontend.ViewModelProduct

@Composable
fun Add(
    uiState: UiStateProductAdd,
    navController: NavHostController,
    viewModelProduct: ViewModelProduct
) {

        if(viewModelProduct.isAlertShow)
          alertAdd(viewModelProduct)

    viewModelProduct.canAddProductButton()

    when(uiState){
        is UiStateProductAdd.Loading -> LoadingScreenAdd(viewModelProduct)
        is UiStateProductAdd.Success -> ResultScreenAdd(viewModelProduct,navController)
        is UiStateProductAdd.Error -> ErrorScreenAdd()

    }

}

@Composable
fun ErrorScreenAdd() {
    Text(text = "ERRORE")
}

@Composable
fun ResultScreenAdd(viewModelProduct: ViewModelProduct, navController: NavHostController) {
    viewModelProduct.saveProductSuccess(navController)
    


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreenAdd(viewModelProduct: ViewModelProduct) {
    val contentResolver = LocalContext.current.contentResolver
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {

            /**nomeProdotto*/
            Text(text = "Prodotto:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            TextField(
                value = viewModelProduct.nomeProdotto,
                onValueChange = { viewModelProduct.nomeProdotto = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions.Default

            )

            Spacer(modifier = Modifier.height(5.dp))

            Divider(
                modifier = Modifier.height(5.dp),
                color = Color(0xFF007782)
            )

            Spacer(modifier = Modifier.height(5.dp))

            /**Prezzo*/
            Text(text = "Prezzo:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            TextField(
                value = viewModelProduct.prezzo,
                onValueChange = { viewModelProduct.prezzo = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions.Default
            )

            Spacer(modifier = Modifier.height(5.dp))

            Divider(
                modifier = Modifier.height(5.dp),
                color = Color(0xFF007782)
            )

            Spacer(modifier = Modifier.height(5.dp))

            /**button send to API*/
            Button(
                onClick = { viewModelProduct.sendProduct(contentResolver) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF007782)),
                enabled = viewModelProduct.isAddEnabled,
            ) {
                Text(text = "Aggiungi Prodotto", color = Color.White)
            }


            //legge immagini dall'archivio del telefono TODO:promeblema con emulatore lista vuota
                val multiplePhotoPick = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(),
                    onResult = { viewModelProduct.immagini = it }
                )
                Button(
                    onClick = { multiplePhotoPick.launch(
                        PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly)
                    ) },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = "Aggiungi immagini", color = Color(0xFF007782), fontWeight = FontWeight.Bold)
                }


                /**visualizza immagini selezionate*/
                LazyRow() {
                    items(viewModelProduct.immagini) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

    }
}

@Composable
private fun alertAdd(viewModelProduct: ViewModelProduct) {


    AlertDialog(
        onDismissRequest = {},
        title = { Text(text ="Prodotto salvato")},
        text = {
            Text(
                text = "Prodotto salvato con successo",
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible)
        },


        /**button di uscita*/
        confirmButton = {
            TextButton(onClick = {  }) {
            }
        }
    )
}

