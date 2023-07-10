package com.example.progettoeafrontend

import android.net.Uri
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.ui.Account
import com.example.progettoeafrontend.ui.Add
import com.example.progettoeafrontend.ui.Home
import com.example.progettoeafrontend.ui.MessageList
import com.example.progettoeafrontend.ui.MessageDetail
import com.example.progettoeafrontend.ui.ProductDetail
import com.example.progettoeafrontend.ui.Search
import com.example.progettoeafrontend.ui.WriteMessage
import com.example.progettoeafrontend.viewmodels.ViewModelLogin
import com.example.progettoeafrontend.viewmodels.ViewModelProduct
import com.example.progettoeafrontend.viewmodels.viewModelMessage

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

enum class ScreenApp(@StringRes val title:Int){
    Home(title = R.string.home),
    Search(title = R.string.search),
    Add(title = R.string.add),
    Message(title = R.string.message),
    Account(title = R.string.account),
    ProductDetail(title = R.string.productDetail),
    MessageDetails(title = R.string.messageDetail),
    WriteMessage(title = R.string.sendMessage)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screenApp() {

    val viewModelProduct: ViewModelProduct = viewModel()
    val viewModelMessage: viewModelMessage = viewModel()
    val viewModelLogin: ViewModelLogin = viewModel()
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
                Home(viewModelProduct.uiStateProduct,navController, viewModelProduct)
            }
            composable(route=ScreenApp.Search.name){
                Search()
            }
            composable(route=ScreenApp.Add.name){
                Add(viewModelProduct.uiStateProductAdd,navController, viewModelProduct)
            }
            composable(route=ScreenApp.Message.name){
                MessageList(viewModelMessage.uiStateMessage,viewModelMessage,navController)
            }
            composable(route=ScreenApp.Account.name){
                Account( viewModelLogin.uiStateAccount,viewModelLogin)
            }
            composable(route= ScreenApp.ProductDetail.name){
                ProductDetail(viewModelProduct.uiStateProductDetail,navController, viewModelProduct)
            }

            /**legge json + cast List<Message>*/
            composable(
                route = "${ScreenApp.MessageDetails.name}/{jsonValue}"
            ) { backStackEntry ->
                val value = Uri.decode(backStackEntry.arguments?.getString("jsonValue") ?: "")
                val messages: List<Message>? = Gson().fromJson(value, object : TypeToken<List<Message>>() {}.type)
                MessageDetail(messages,navController)
            }

            composable(route = "${ScreenApp.WriteMessage.name}?venditoreId={venditoreId}&venditoreNome={venditoreNome}",
                arguments = listOf(
                    navArgument("venditoreId") { type = NavType.LongType },
                    navArgument("venditoreNome") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val venditoreId = backStackEntry.arguments?.getLong("venditoreId") ?: 0L
                val venditoreNome = backStackEntry.arguments?.getString("venditoreNome") ?: ""
                WriteMessage(viewModelMessage, navController, venditoreId, venditoreNome)
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

