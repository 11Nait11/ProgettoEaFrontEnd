package com.example.progettoeafrontend

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.network.Service
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface UiStateMessage {
    data class Success(val map: MutableMap<Pair<String, String>, MutableList<String>>) : UiStateMessage
    object Error : UiStateMessage
    object Loading : UiStateMessage
}

sealed interface UiStateSendMessage {
    object Success : UiStateSendMessage
    object Error : UiStateSendMessage
    object Loading : UiStateSendMessage
}


class viewModelMessage : ViewModel(){

    var uiStateMessage:UiStateMessage by mutableStateOf(UiStateMessage.Loading)
        private set

    var uiStateSendMessage:UiStateSendMessage by mutableStateOf(UiStateSendMessage.Loading)
        private set


    init{
//        getMessages()
    }

    fun getMessages() {

        viewModelScope.launch {
            val map: MutableMap<Pair<String, String>, MutableList<String>> = mutableMapOf()

            uiStateMessage = try {
                val listResult = Service.retrofitService.getMessages(1)
                for(message  in listResult)
                {
                    val key: Pair<String, String> = if (message.mittenteNome == "Paperino") {//sosistuire con utente loggato
                        Pair(message.mittenteNome, message.destinatarioNome)
                    } else {
                        Pair(message.destinatarioNome, message.mittenteNome)
                    }
                    val value: MutableList<String> = map[key] ?: mutableListOf()
                    value.add(message.testo)
                    map[key] = value

                }//for
                UiStateMessage.Success(map)
            } catch (e: IOException) { UiStateMessage.Error }
        }
    }



    fun sendMessage(message: String, venditoreId: Long, venditoreNome: String) {
        val m=Message(testo=message, mittenteId = 1, destinatarioId = venditoreId)
        viewModelScope.launch {
            uiStateSendMessage = try {
                Service.retrofitService.saveMessage(m)
                UiStateSendMessage.Success
            } catch (e: IOException) { UiStateSendMessage.Error }
        }
    }

    fun refresh() {
        uiStateMessage=UiStateMessage.Loading
        getMessages()
    }
    fun reset() {
        uiStateSendMessage=UiStateSendMessage.Loading
    }


}