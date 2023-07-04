package com.example.progettoeafrontend

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.model.User
import com.example.progettoeafrontend.network.Service
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface UiStateLogin{
    object Login:UiStateLogin
    object Success : UiStateLogin
    object Error : UiStateLogin
    object Loading : UiStateLogin
}

sealed interface UiStateAccount{
    data class Success(val utente:User) : UiStateAccount
    object Error : UiStateAccount
    object Loading : UiStateAccount
}

private enum class AppState {
    LOGIN, APP
}

class ViewModelLogin : ViewModel(){

    var uiStateLogin: UiStateLogin by mutableStateOf(UiStateLogin.Login)
        private set
    var uiStateAccount: UiStateAccount by mutableStateOf(UiStateAccount.Loading)
        private set

    var usernameState by mutableStateOf("")
    var passwordState by mutableStateOf("")

    var isLoginEnabled by  mutableStateOf(false)
        private set

    init {}

    fun sendLogin() {
        viewModelScope.launch {
            uiStateLogin = try {

                val credentials = "$usernameState:$passwordState"
                var credentials64 ="Basic "+Base64.encodeToString(credentials.toByteArray(), Base64.DEFAULT).trim()


                val headers=Service.retrofitService.login(credentials64).headers()
                Service.refreshToken = "Bearer "+headers.get("refresh_token")
                Service.accessToken = "Bearer "+headers.get("access_token")

                if (Service.accessToken != null) {
                    Log.d("login", "accessToken salvato : $Service.accessToken")
                    //popola la pagina account
                    getUser()
                    UiStateLogin.Success
                } else {
                    Log.d("login", "Token vuoto")
                    UiStateLogin.Error
                }

            }
            catch (e: IOException) { UiStateLogin.Error }
            catch (e: HttpException) { UiStateLogin.Error }
        }
    }


    fun getUser()
    {
            viewModelScope.launch {
                uiStateAccount = try {
                    var utente = Service.retrofitService.getUtente(usernameState)
                    Service.accessId=utente.id
                    Service.accessNome=utente.nome
                    Log.d("login","Utente loggato : $Service.accessId $Service.accessNome")
                    UiStateAccount.Success(utente)

                } catch (e: IOException) {
                    UiStateAccount.Error
                } catch (e: HttpException) {
                    UiStateAccount.Error
                }

            }
    }


    fun canSendButton() {
        val isUsernameNotEmpty = usernameState.isNotEmpty()
        val isPasswordNotEmpty = passwordState.isNotEmpty()
        isLoginEnabled = isUsernameNotEmpty && isPasswordNotEmpty
    }


}
