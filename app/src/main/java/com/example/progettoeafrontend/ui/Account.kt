package com.example.progettoeafrontend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progettoeafrontend.R
import com.example.progettoeafrontend.UiStateAccount
import com.example.progettoeafrontend.ViewModelLogin
import com.example.progettoeafrontend.model.User


@Composable
fun Account(uiState:UiStateAccount, viewModelLogin: ViewModelLogin) {


    if (uiState == UiStateAccount.Loading || uiState == UiStateAccount.Error)
        viewModelLogin.getUser()

    when (uiState) {
        is UiStateAccount.Loading -> LoadingScreenAccount()
        is UiStateAccount.Success -> ResultScreenAccount(uiState.utente)
        is UiStateAccount.Error -> ErrorScreenAccount()
    }
}


@Composable
fun ResultScreenAccount(utente: User) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ){
        item{

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { }
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 50.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Info Utente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .border(
                        width = 3.dp,
                        color = Color(0xFF007782),
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Nome:", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start).padding(start = 12.dp))
                Text(text = utente.nome, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.Start).padding(start = 60.dp))

                Spacer(modifier = Modifier.height(20.dp))


                Text(text = "Cognome:", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start).padding(start = 12.dp))
                Text(text = utente.cognome, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.Start).padding(start = 60.dp))

                Spacer(modifier = Modifier.height(20.dp))


                Text(text = "Mail:", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start).padding(start = 12.dp))
                Text(text = utente.email, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.Start).padding(start = 60.dp))

                Spacer(modifier = Modifier.height(5.dp))


            }


        }

    }

}


@Composable
fun LoadingScreenAccount(modifier: Modifier = Modifier) {
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
fun ErrorScreenAccount(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(stringResource(R.string.failed))
            IconButton(onClick = { }) {//todo inserire logica button
                Icon(Icons.Default.Refresh, contentDescription = stringResource(id = R.string.refresh))
            }
        }
    }
}






