package com.example.progettoeafrontend

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.progettoeafrontend.ui.Account
import com.example.progettoeafrontend.ui.Add
import com.example.progettoeafrontend.ui.Home
import com.example.progettoeafrontend.ui.Message
import com.example.progettoeafrontend.ui.ProductDetail
import com.example.progettoeafrontend.ui.Search

import com.example.progettoeafrontend.ui.theme.ProgettoEaFrontEndTheme


enum class ScreenApp(@StringRes val title:Int){
    Home(title = R.string.home),
    Search(title = R.string.search),
    Add(title = R.string.add),
    Message(title = R.string.message),
    Account(title = R.string.account),
    ProductDetail(title = R.string.productDetail)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenApp() {

    val appViewModel:AppViewModel = viewModel()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
               )  {
                iconButton(icon = Icons.Filled.Home, onClick = {navController.navigate(ScreenApp.Home.name)}, R.string.home)
                iconButton(icon = Icons.Filled.Search, onClick = {navController.navigate(ScreenApp.Search.name)}, R.string.search)
                iconButton(icon = Icons.Filled.Add, onClick = {navController.navigate(ScreenApp.Add.name)}, R.string.add)
                iconButton(icon = Icons.Filled.Email, onClick = {navController.navigate(ScreenApp.Message.name)}, R.string.message)
                iconButton(icon = Icons.Filled.AccountBox, onClick = {navController.navigate(ScreenApp.Account.name)}, R.string.account)
            }


        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenApp.Home.name,
            modifier = Modifier.padding(innerPadding),
        ){

            composable(route=ScreenApp.Home.name){
                Home(appViewModel.uiStateImage,navController, appViewModel)
            }

            composable(route=ScreenApp.Search.name){
                Search()
            }
            composable(route=ScreenApp.Add.name){
                Add()
            }
            composable(route=ScreenApp.Message.name){
                Message()


            }
            composable(route=ScreenApp.Account.name){
                Account()
            }
            composable(route= ScreenApp.ProductDetail.name){
                ProductDetail(appViewModel.uiStateProd,navController, appViewModel)
            }
        }

    }
}



@Composable
fun iconButton(
    icon: ImageVector,
    onClick:() -> Unit,
    @StringRes label: Int,

){
    IconButton(
        onClick = onClick,
    ) {

        Column(Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,) {
            Icon(icon, contentDescription = stringResource(label))
            Text(text = stringResource(id = label), fontSize = 9.sp)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ProgettoEaFrontEndTheme {
        screenApp()
    }
}