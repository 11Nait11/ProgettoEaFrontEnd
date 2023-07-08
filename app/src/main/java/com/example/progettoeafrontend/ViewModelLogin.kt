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

    /**tiene traccia dello stato corrente della UI*/
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

    /**disabilitano button in caso di campi null di login e register */
    var isLoginEnabled by  mutableStateOf(false)
        private set
    var isRegisterEnabled by  mutableStateOf(false)
        private set

    /**mostrano alert per login e registrazione*/
    var isAlertShowRegister by  mutableStateOf(false)
        private set
    var isAlertShowLogin by  mutableStateOf(false)
        private set

    init {}

    /**ritorna alla login se la registrazione e' andata  a buon fine*/
    fun goToLoginAfterRegister(){
        setShowAlertLogin(true) //registrazione completata tutto ok
        flush()
        uiStateLogin=UiStateLogin.Login
    }

    /**contatta api per salvare nuovo utente*/
    fun sendRegister()
    {
        viewModelScope.launch {
            uiStateRegister = try {
                val user = User(id = 0, nome = firstname, cognome = lastname, email = mail, password = password)
                Service.retrofitService.sendRegister(user)
                UiStateRegister.Success
            }
            catch (e: IOException) { UiStateRegister.Error }
            catch (e: HttpException) { UiStateRegister.Error }
        }
    }


    /**invia credenzili e se autenticato riceve token*/
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


    /**ottiene utente cercando per username(email)*/
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

    /**ripulisce i campi del form register*/
    fun flush() {
        firstname = ""
        lastname = ""
        mail = ""
        password = ""
    }
    /**Button (registrati) nella LoginScreen, chiama Registrazione*/
    fun register() {
        uiStateLogin=UiStateLogin.Register
        uiStateRegister=UiStateRegister.Loading
    }
    /**Button (Ho gia un account) da Registrazione ritorna a LoginScreen*/
    fun setUistateLogin(){
        uiStateLogin=UiStateLogin.Login
    }

    /**gestisce la visualizzazione degli alert*/
    fun setShowAlertRegister(v:Boolean){
        isAlertShowRegister=v
    }
    fun setShowAlertLogin(v:Boolean){
        isAlertShowLogin=v
    }

    /** verifica campi username e password*/
    fun canSendButton() {
        val isUsernameNotEmpty = usernameState.isNotEmpty()
        val isPasswordNotEmpty = passwordState.isNotEmpty()
        isLoginEnabled = isUsernameNotEmpty && isPasswordNotEmpty
    }

    /** verifica campi form register*/
    fun canRegisterButton() {
        val a = firstname.isNotEmpty()
        val b = lastname.isNotEmpty()
        val c = mail.isNotEmpty()
        val d = password.isNotEmpty()
        isRegisterEnabled = a && b && c && d
    }

    /**valida email e password nel from register*/
    fun validateInput(): List<ErrorMess> {
        val errors = mutableListOf<ErrorMess>()
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            errors.add(ErrorMess("Mail", "email non valida"))
            setShowAlertRegister(true)
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,}$".toRegex())) {
            errors.add(ErrorMess("Password", "La password deve contenere almeno 8 caratteri, una lettera maiuscola e un carattere speciale (!@#%^&+=)"))
            setShowAlertRegister(true)
        }
        return errors }
}


data class ErrorMess(
    val field: String,
    val message: String
)