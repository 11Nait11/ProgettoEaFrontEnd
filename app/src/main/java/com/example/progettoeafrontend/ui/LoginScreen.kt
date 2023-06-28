package com.example.progettoeafrontend.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateLogin
import com.example.progettoeafrontend.screenApp
import com.example.progettoeafrontend.ViewModelLogin


@Composable
fun LoginScreen(){

    val viewModelLogin: ViewModelLogin = viewModel()
    viewModelLogin.canSendButton()

    when(viewModelLogin.uiStateLogin){
        is UiStateLogin.Login -> Login(viewModelLogin)
        is UiStateLogin.Loading -> LoadingScreenLogin()
        is UiStateLogin.Success -> ResultScreenLogin()
        is UiStateLogin.Error -> ErrorScreenLogin(viewModelLogin=viewModelLogin)

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(viewModelLogin: ViewModelLogin) {
    LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Corpo(viewModelLogin)
            }
        }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Corpo(viewModelLogin: ViewModelLogin){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_login),
            contentDescription = stringResource(id = R.string.login),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .scale(2f) // serve a ingrandire l'immagine
        )

        Spacer(modifier = Modifier.height(87.dp))

        Text(
            text = "Vendi vestiti di seconda mano completamente gratis",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))



        /**INIZIO FORM LOGIN*/



            //username
            TextField(
                value = viewModelLogin.usernameState,
                onValueChange = { viewModelLogin.usernameState = it },
                label = { Text("Username") },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //password
            TextField(
                value = viewModelLogin.passwordState,
                onValueChange = { viewModelLogin.passwordState = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { viewModelLogin.sendLogin() }),
                modifier = Modifier.padding(bottom = 16.dp)
            )


            Button(
                onClick = { viewModelLogin.sendLogin() },
                enabled = viewModelLogin.isLoginEnabled, // Abilita/Disabilita il pulsante(se i campi sono vuoti)
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
            ) {
                Text("Login",color= Color(0xFF007782))
            }


        /**Fine FORM*/

        Button(
            onClick = { /*TODO*/ }, //Bottone che riporta alla pagina anteprima messaggio -> da implementare
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF007782))
        ) {
            Text(
                text = "Iscriviti a Vinted", color = Color.White,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        //Testo cliccabile che riporta alla pagina guida_sito.kt

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Informazioni su Vinted: ",
                color = Color.Gray,
                fontSize = 15.sp
            )
            ClickableText(
                text = AnnotatedString("Guida al sito"),
                onClick = {
                    // Aggiungere il codice per passare al file guida_sito
                },
                modifier = Modifier.clickable { },
                style = TextStyle(
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Black
                )
            )

        }

    }


}



@Composable
fun ResultScreenLogin() {
    screenApp()
//    Text(text = "ciao")

}
@Composable
fun LoadingScreenLogin(modifier: Modifier = Modifier) {
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
fun ErrorScreenLogin(modifier: Modifier = Modifier, viewModelLogin: ViewModelLogin) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Errore di accesso",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Credenziali non valide",
                color = Color.Red
            )
        }
    }
    Login(viewModelLogin = viewModelLogin)
    Spacer(modifier = Modifier.height(16.dp))
}