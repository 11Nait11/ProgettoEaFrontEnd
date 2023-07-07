package com.example.progettoeafrontend.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progettoeafrontend.UiStateRegister
import com.example.progettoeafrontend.ViewModelLogin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registrazione(viewModelLogin: ViewModelLogin) {

    viewModelLogin.canRegisterButton()

    if(viewModelLogin.isAlertShowRegister)
        alert(viewModelLogin)

    Log.d("pop","uistate register ${viewModelLogin.uiStateRegister}")
    when (viewModelLogin.uiStateRegister) {
        is UiStateRegister.Loading -> LoadingScreenRegister(viewModelLogin)
        is UiStateRegister.Success -> ResultScreenRegister(viewModelLogin)
        is UiStateRegister.Error -> ErrorScreenRegister()

    }
}

@Composable
fun ResultScreenRegister(viewModelLogin: ViewModelLogin){
    viewModelLogin.goToLoginAfterRegister()


}
@Composable
fun ErrorScreenRegister(){
    Text(text = "errore")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreenRegister(viewModelLogin: ViewModelLogin){

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))


                Text(text = "Nome:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = viewModelLogin.firstname,
                    onValueChange = { viewModelLogin.firstname = it },
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



                Text(text = "Cognome:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = viewModelLogin.lastname,
                    onValueChange = { viewModelLogin.lastname = it },
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



                Text(text = "Mail:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = viewModelLogin.mail,
                    onValueChange = { viewModelLogin.mail = it },
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



                Text(text = "Password:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = viewModelLogin.password,
                    onValueChange = { viewModelLogin.password = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        viewModelLogin.validateInput()
                        if(!viewModelLogin.isAlertShowRegister) {
                            viewModelLogin.sendRegister()
                        }
                    }),
                )

                Spacer(modifier = Modifier.height(5.dp))
                Divider(modifier = Modifier.height(5.dp), color = Color(0xFF007782))
                Spacer(modifier = Modifier.height(10.dp))


            }


            Column {
                /**registrati button*/
                Button(
                    onClick = {
                        viewModelLogin.validateInput()
                        if(!viewModelLogin.isAlertShowRegister)
                            viewModelLogin.sendRegister()
                              },
                    enabled = viewModelLogin.isRegisterEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007782))
                ) {
                    Text(text = "Registrati")
                }

                /**torna alla login*/
                Button(
                    onClick = {viewModelLogin.login()},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007782))
                ) {
                    Text(text = "Ho gia un account")
                }

            }
        }
    }
}

@Composable
private fun alert(
//    title:String,
//    text:String,
//    onClick: ()->Unit
    viewModelLogin: ViewModelLogin
) {

    /**visualizza Messaggio di avvenuto pagamento o di errore */
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = viewModelLogin.validateInput()[0].field)},
        text = {
             Text(
                    text = viewModelLogin.validateInput()[0].message,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible)

        },

        /**button di uscita*/
        confirmButton = {
            TextButton(onClick = { viewModelLogin.showAlertRegister(false) }) {
                Text(text = "OK")
            }
        }
    )
}


