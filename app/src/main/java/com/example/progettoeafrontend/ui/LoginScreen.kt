package com.example.progettoeafrontend.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = viewModelLogin.usernameState,
            onValueChange = { viewModelLogin.usernameState = it },
            label = { Text("Username") },
            modifier = Modifier.padding(bottom = 16.dp)
        )


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
            enabled = viewModelLogin.isLoginEnabled, // Abilita/Disabilita il pulsante
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Login")
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