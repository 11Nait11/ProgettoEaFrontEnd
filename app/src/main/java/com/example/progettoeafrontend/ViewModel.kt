package com.example.progettoeafrontend

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.network.Image
import com.example.progettoeafrontend.network.Service
import com.example.progettoeafrontend.network.Utente
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface UiState {
    data class Success (val utenti: List<Image>) : UiState
    object Error : UiState
    object Loading : UiState
}

class AppViewModel : ViewModel(){

    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    init {
        getImages()
    }

//    private fun getUtenti() {
//        viewModelScope.launch {
//            uiState = try {
//                val listResult = Service.retrofitService.getUtenti()
//                UiState.Success(listResult)
//            } catch (e: IOException) { UiState.Error }
//        }
//    }
    private fun getImages() {
        viewModelScope.launch {
            uiState = try {
                val listResult = Service.retrofitService.getImages()
                UiState.Success(listResult)
            } catch (e: IOException) { UiState.Error }
        }
    }
}