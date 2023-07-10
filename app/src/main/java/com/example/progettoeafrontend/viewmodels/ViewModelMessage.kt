package com.example.progettoeafrontend.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.network.Service
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface UiStateMessage {
    data class Success(val map: MutableMap<Pair<String, String>, MutableList<Message>>) :
        UiStateMessage
    object Error : UiStateMessage
    object Loading : UiStateMessage
}

sealed interface UiStateSendMessage {
    object Success : UiStateSendMessage
    object Error : UiStateSendMessage
    object Loading : UiStateSendMessage
}


class viewModelMessage : ViewModel(){

    var uiStateMessage: UiStateMessage by mutableStateOf(UiStateMessage.Loading)
        private set

    var uiStateSendMessage: UiStateSendMessage by mutableStateOf(UiStateSendMessage.Loading)
        private set


    init{
//        getMessages()
    }

    /**ottieni messsaggi da backEnd, costruisce mappa per ordinare visualizzazione anteprima MessageList*/
    fun getMessages() {
        viewModelScope.launch {
            val map: MutableMap<Pair<String, String>, MutableList<Message>> = mutableMapOf()

            uiStateMessage = try {
                val userMessageList = Service.retrofitService.getMessages(Service.accessId)
                /**ragruppa messaggi stessa conversazione*/
                for (userMessage in userMessageList) {
                    /**costruisce chiave  1,2 = 2,1 (stessa Conversazione)*/
                    val key: Pair<String, String> =
                        if (userMessage.mittenteNome == Service.accessNome) //TODO:sosistuire con utenteLoggato
                            Pair(userMessage.mittenteNome, userMessage.destinatarioNome)
                        else
                            Pair(userMessage.destinatarioNome, userMessage.mittenteNome)

                    /**crea conversazione se non mappata, altrimenti aggiunge a conversazione gia mappata*/
                    val listaConversazione: MutableList<Message> = map[key] ?: mutableListOf()
                    listaConversazione.add(userMessage)
                    map[key] = listaConversazione

                }
                UiStateMessage.Success(map)//anteprima messaggi raggruppati per conversazione
            } catch (e: IOException) {
                UiStateMessage.Error
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.d("refreshToken", "refreshToken 401")
                    Service.refreshToken()
                    UiStateMessage.Error
                } else
                    UiStateMessage.Error
            }
        }
    }

        /**@Post backEnd save  */
        fun sendMessage(message: String, venditoreId: Long) {
            val m = Message(
                testo = message,
                mittenteId = Service.accessId,
                destinatarioId = venditoreId
            )//TODO:inserire mittenteId=utenteLoggato
            viewModelScope.launch {
                uiStateSendMessage = try {
                    Service.retrofitService.saveMessage(m)
                    UiStateSendMessage.Success
                } catch (e: IOException) {
                    UiStateSendMessage.Error
                } catch (e: HttpException) {
                    if (e.code() == 401) {
                        Log.d("refreshToken", "refreshToken 401")
                        Service.refreshToken()
                        UiStateSendMessage.Error
                    } else
                        UiStateSendMessage.Error
                }
            }
        }


        /**ricontatta backEnd per ottenere eventuali messaggi nuovi*/
        fun setLoadingMessageState() {
            uiStateMessage = UiStateMessage.Loading
        }

        fun setLoadingSendMessageState() {
            uiStateSendMessage = UiStateSendMessage.Loading
        }


    }