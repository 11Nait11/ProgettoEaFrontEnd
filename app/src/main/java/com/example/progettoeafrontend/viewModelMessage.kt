package com.example.progettoeafrontend

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.network.Service
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface UiStateMessage {
    data class Success (val resultList: List<Any>,) : UiStateMessage
    object Error : UiStateMessage
    object Loading : UiStateMessage
}


class viewModelMessage : ViewModel(){

    var uiStateMessage:UiStateMessage by mutableStateOf(UiStateMessage.Loading)
        private set

    init{
//        getMessages()
    }

    fun getMessages() {
//    avvio operaz. asyn per getImages()
        viewModelScope.launch {
            uiStateMessage = try {
                val listResult = Service.retrofitService.getMessages(1)
                UiStateMessage.Success(resultList = listResult)
            } catch (e: IOException) { UiStateMessage.Error }
        }
    }


}