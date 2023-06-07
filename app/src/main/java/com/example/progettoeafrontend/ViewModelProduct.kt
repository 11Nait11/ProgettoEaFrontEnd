package com.example.progettoeafrontend

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.network.Service

import kotlinx.coroutines.launch
import java.io.IOException


sealed interface UiStateProduct {
    data class Success (val resultList: List<Any>,) : UiStateProduct
    object Error : UiStateProduct
    object Loading : UiStateProduct
}
sealed interface UiStateProductDetail {
    data class Success (val result: Any) : UiStateProductDetail
    object Error : UiStateProductDetail
    object Loading : UiStateProductDetail
}

class viewModelProduct : ViewModel(){

    var uiStateProduct: UiStateProduct by mutableStateOf(UiStateProduct.Loading)
        private set
    var uiStateProductDetail: UiStateProductDetail by mutableStateOf(UiStateProductDetail.Loading)
        private set

    var comprato by  mutableStateOf(false)

    init {getProducts()}

    fun setComprato(){ comprato=true }

    fun setUiStateProductDetail(product: Product){
        uiStateProductDetail=UiStateProductDetail.Success(product)
    }


     fun getImages() {
//    avvio operaz. asyn per getImages()
        viewModelScope.launch {
            Log.d("Pippo","getimages")
            uiStateProduct = try {
                Log.d("Pippo","entroImage")
                val listResult = Service.retrofitService.getImages()
                Log.d("Pippo","ottengoImage")
                UiStateProduct.Success(resultList = listResult)
            } catch (e: IOException) { UiStateProduct.Error }
        }
    }





    fun getProducts() {
//    avvio operaz. asyn per getImages()
        viewModelScope.launch {
            uiStateProduct = try {
                val listResult = Service.retrofitService.getProducts()
                UiStateProduct.Success(resultList = listResult)
            } catch (e: IOException) { UiStateProduct.Error }
        }
    }

    fun getProductDetail(prodottoId:Long){
//        gestire diversamente ?
        uiStateProductDetail=UiStateProductDetail.Loading
        viewModelScope.launch {
            Log.d("Pippo","getProd")
            uiStateProductDetail = try {
                Log.d("Pippo","entroPRdo")
                val result = Service.retrofitService.getProduct(prodottoId)
                Log.d("Pippo","ottengoProd")
                UiStateProductDetail.Success(result= result)
            } catch (e: IOException) { UiStateProductDetail.Error }
        }

    }

    fun deleteProduct(idProd: Long) {
            viewModelScope.launch {
                uiStateProduct = try {
                    Service.retrofitService.deleteProduct(idProd)
                    UiStateProduct.Loading
                } catch (e: IOException) { UiStateProduct.Error }
            }
        Log.d("DELETE", "Porodotto Cancellato " +idProd)
    }


}