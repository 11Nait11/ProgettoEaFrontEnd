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
import retrofit2.HttpException
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

class ViewModelProduct : ViewModel(){

    var uiStateProduct: UiStateProduct by mutableStateOf(UiStateProduct.Loading)
        private set
    var uiStateProductDetail: UiStateProductDetail by mutableStateOf(UiStateProductDetail.Loading)
        private set


    var comprato by  mutableStateOf(false)
        private set

    init {getProducts()}



    /**setComprato per click Button in Prodotto*/
    fun setCompratoTrue(){ comprato=true }
    fun setCompratoFalse(){ comprato=false }


    /**navigazione info prodotto */
    fun setUiStateProductDetail(product: Product){
        uiStateProductDetail=UiStateProductDetail.Success(product)
    }


    /**ricarica prodotto da backEnd*/
    fun refresh() {
        uiStateProduct=UiStateProduct.Loading
        getProducts()
    }


    fun getProducts() {
        viewModelScope.launch {
            uiStateProduct = try {
                val listResult = Service.retrofitService.getProducts()
                UiStateProduct.Success(resultList = listResult)
            } catch (e: IOException) { UiStateProduct.Error }
              catch (e: HttpException) {
                  if(e.code()==401){
                      Log.d("refreshToken","refreshToken 401")
                      Service.refreshToken()
                      setCompratoFalse()
                      UiStateProduct.Error

                  }
                  else
                  UiStateProduct.Error }
        }
    }

    fun deleteProduct(idProd: Long) {
            viewModelScope.launch {
                uiStateProduct = try {
                    Service.retrofitService.deleteProduct(idProd)
                    UiStateProduct.Loading
                }
                catch (e: IOException) { UiStateProduct.Error }
                catch (e: HttpException) {
                    if(e.code()==401){
                        Log.d("refreshToken","refreshToken 401")
                        Service.refreshToken()
                        UiStateProduct.Error
                    }
                    else
                        UiStateProduct.Error }
            }
        Log.d("DELETE", "Porodotto Cancellato " +idProd)
    }



/**logica vecchia - da rivedere*/
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

    /**get singolo prodotto forse non necessaria - da rivedere */
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



}