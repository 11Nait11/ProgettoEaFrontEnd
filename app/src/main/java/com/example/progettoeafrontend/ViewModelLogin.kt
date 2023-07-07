package com.example.progettoeafrontend

import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.model.User
import com.example.progettoeafrontend.network.Service
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface UiStateLogin{
    object Login:UiStateLogin
    object Register:UiStateLogin
    object Success : UiStateLogin
    object Error : UiStateLogin
    object Loading : UiStateLogin
}
sealed interface UiStateAccount{
    data class Success(val utente:User) : UiStateAccount
    object Error : UiStateAccount
    object Loading : UiStateAccount
}
sealed interface UiStateRegister{
    object Success : UiStateRegister
    object Error : UiStateRegister
    object Loading : UiStateRegister
}


class ViewModelLogin : ViewModel(){

    var uiStateRegister: UiStateRegister by mutableStateOf(UiStateRegister.Loading)
        private set
    var uiStateLogin: UiStateLogin by mutableStateOf(UiStateLogin.Login)
        private set
    var uiStateAccount: UiStateAccount by mutableStateOf(UiStateAccount.Loading)
        private set


    /**login*/
    var usernameState by mutableStateOf("")
    var passwordState by mutableStateOf("")
    /**registrazione*/
    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var mail by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoginEnabled by  mutableStateOf(false)
        private set
    var isRegisterEnabled by  mutableStateOf(false)
        private set
    var isAlertShowRegister by  mutableStateOf(false)
        private set
    var isAlertShowLogin by  mutableStateOf(false)
        private set

    init {}

    fun goToLoginAfterRegister(){
        showAlertLogin(true)
        flush()
        uiStateLogin=UiStateLogin.Login

    }

    fun sendRegister()
    {
        viewModelScope.launch {
            uiStateRegister = try {
                val user = User(id = 0, nome = firstname, cognome = lastname, email = mail, password = password)
                Log.d("login","prima register ")
                Service.retrofitService.sendRegister(user)
                UiStateRegister.Success
            }
            catch (e: IOException) { UiStateRegister.Error }
            catch (e: HttpException) { UiStateRegister.Error }
        }
    }


    fun sendLogin() {
        viewModelScope.launch {
            uiStateLogin = try {

                val credentials = "$usernameState:$passwordState"
                var credentials64 ="Basic "+Base64.encodeToString(credentials.toByteArray(), Base64.DEFAULT).trim()

                //invio request e prendo header response
                val headers=Service.retrofitService.login(credentials64).headers()
                Service.refreshToken = headers.get("refresh_token")
                Service.accessToken =  headers.get("access_token")

                if (Service.accessToken != null) {
                    Log.d("login", "accessToken salvato : ${Service.accessToken}")
                    getUser()//popola la pagina Account
                    UiStateLogin.Success
                } else {
                    Log.d("login", "Token vuoto")
                    UiStateLogin.Error
                }

            }
            catch (e: IOException) { UiStateLogin.Error }
            catch (e: HttpException) {
                if(e.code()==401){
                    Log.d("refreshToken","refreshToken 401")
                    Service.refreshToken()
                    UiStateLogin.Error
                }
                else
                    UiStateLogin.Error }
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
                }  catch (e: HttpException) {
                    if(e.code()==401){
                        Log.d("refreshToken","refreshToken 401")
                        Service.refreshToken()
                        UiStateAccount.Error
                    }
                    else
                        UiStateAccount.Error }
            }
    }


    fun flush() {
        firstname = ""
        lastname = ""
        mail = ""
        password = ""
    }
    fun register() {
        uiStateLogin=UiStateLogin.Register
        uiStateRegister=UiStateRegister.Loading
    }
    fun login(){
        uiStateLogin=UiStateLogin.Login
    }
    fun showAlertRegister(v:Boolean){
        isAlertShowRegister=v
    }
    fun showAlertLogin(v:Boolean){
        isAlertShowLogin=v
    }


    fun canSendButton() {
        val isUsernameNotEmpty = usernameState.isNotEmpty()
        val isPasswordNotEmpty = passwordState.isNotEmpty()
        isLoginEnabled = isUsernameNotEmpty && isPasswordNotEmpty
    }
    fun canRegisterButton() {
        val a = firstname.isNotEmpty()
        val b = lastname.isNotEmpty()
        val c = mail.isNotEmpty()
        val d = password.isNotEmpty()
        isRegisterEnabled = a && b && c && d
    }

    fun validateInput(): List<ErrorMess> {
        val errors = mutableListOf<ErrorMess>()
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            errors.add(ErrorMess("Mail", "email non valida"))
            showAlertRegister(true)
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,}$".toRegex())) {
            errors.add(ErrorMess("Password", "La password deve contenere almeno 8 caratteri, una lettera maiuscola e un carattere speciale (!@#%^&+=)"))
            showAlertRegister(true)
        }



        return errors }

}

data class ErrorMess(
    val field: String,
    val message: String
)