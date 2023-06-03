package com.example.progettoeafrontend

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.network.Service
import kotlinx.coroutines.launch
import java.io.IOException



sealed interface UiStateImage {
    data class Success (val resultList: List<Any>,) : UiStateImage
    object Error : UiStateImage
    object Loading : UiStateImage
}
sealed interface UiStateProd {
    data class Success (val result: Any) : UiStateProd
    object Error : UiStateProd
    object Loading : UiStateProd
}

class AppViewModel : ViewModel(){

    var uiStateImage: UiStateImage by mutableStateOf(UiStateImage.Loading)
        private set
    var uiStateProd: UiStateProd by mutableStateOf(UiStateProd.Loading)
        private set

    init {
        getImages()

    }


     fun getImages() {
//    avvio operaz. asyn per getImages()
        viewModelScope.launch {
            Log.d("Pippo","getimages")
            uiStateImage = try {
                Log.d("Pippo","entroImage")
                val listResult = Service.retrofitService.getImages()
                Log.d("Pippo","ottengoImage")
                UiStateImage.Success(resultList = listResult)
            } catch (e: IOException) { UiStateImage.Error }
        }
    }

    fun getProdotto(prodottoId:Long){
        uiStateProd=UiStateProd.Loading
        viewModelScope.launch {
            Log.d("Pippo","getProd")
            uiStateProd = try {
                Log.d("Pippo","entroPRdo")
                val result = Service.retrofitService.getProduct(prodottoId)
                Log.d("Pippo","ottengoProd")
                UiStateProd.Success(result= result)
            } catch (e: IOException) { UiStateProd.Error }
        }

    }


}