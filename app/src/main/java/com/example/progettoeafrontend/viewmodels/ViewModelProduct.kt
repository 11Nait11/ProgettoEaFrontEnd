package com.example.progettoeafrontend.viewmodels

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.progettoeafrontend.ScreenApp
import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.network.Service

import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException

sealed interface UiStateProductAdd {
    object Success  : UiStateProductAdd
    object Error : UiStateProductAdd
    object Loading : UiStateProductAdd
}
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
    var uiStateProductAdd: UiStateProductAdd by mutableStateOf(UiStateProductAdd.Loading)
        private set
    var uiStateProductDetail: UiStateProductDetail by mutableStateOf(UiStateProductDetail.Loading)
        private set
    var comprato by  mutableStateOf(false)
        private set


    /**abilita button registra prodotto  */
    var isAddEnabled by  mutableStateOf(false)
        private set

    /**mostrano alert */
    var isAlertShow by  mutableStateOf(false)
        private set
//
    fun setShowAlertAdd(v:Boolean){
        isAlertShow=v
    }

    init {getProducts()}

    /**campi form aggiungi prodotto */
    var nomeProdotto by mutableStateOf("")
    var prezzo by mutableStateOf("")
    var immagini by mutableStateOf<List<Uri?>>(emptyList())





    /**setComprato per click Button in Prodotto*/
    fun setCompratoTrue(){ comprato=true }
    fun setCompratoFalse(){ comprato=false }

    /**navigazione info prodotto */
    fun setUiStateProductDetail(product: Product){
        uiStateProductDetail= UiStateProductDetail.Success(product)
    }
    fun setUiStateProductLoading(){
        uiStateProduct= UiStateProduct.Loading
    }
    /**ricarica prodotto da backEnd*/
    fun refresh() {
        uiStateProduct= UiStateProduct.Loading
        getProducts()
    }

    /**ottiene tutti i prodotti*/
    fun getProducts() {
        viewModelScope.launch {
            uiStateProduct = try {
                val listResult = Service.retrofitService.getProducts()
                UiStateProduct.Success(resultList = listResult)
            } catch (e: IOException) {
                UiStateProduct.Error
            }
            catch (e: HttpException) {
                if(e.code()==401){
                    Log.d("refreshToken","refreshToken 401")
                    Service.refreshToken()
                    UiStateProduct.Error
                }
                else
                    UiStateProduct.Error
            }
        }
    }

    /**cancella prodotto dal db (usato anche in caso di vendita prodotto)*/
    fun deleteProduct(idProd: Long) {
            viewModelScope.launch {
                uiStateProduct = try {
                    Service.retrofitService.deleteProduct(idProd)
                    UiStateProduct.Loading
                }
                catch (e: IOException) {
                    UiStateProduct.Error
                }
                catch (e: HttpException) {
                    if(e.code()==401){
                        Log.d("refreshToken","refreshToken 401")
                        Service.refreshToken()
                        UiStateProduct.Error
                    }
                    else
                        UiStateProduct.Error
                }
            }
        Log.d("DELETE", "Porodotto Cancellato " +idProd)
    }


    /**invia prodotto al backEnd per salvarlo*/
     fun sendProduct(contentResolver: ContentResolver) {
        /**image in base64 to send API*/
        val images: MutableList<Image> = mutableListOf()
        for (immagineUri in immagini) {
            val base64String = immagineUri?.let { convertUriToBase64(contentResolver, it) }
            if (base64String != null) {
                val image = Image(0L, base64String, 0L)
                images.add(image) }
        }
        val product= Product(0L,nomeProdotto,prezzo.toDouble(),"ciao mamma guarda come mi diverto",Service.accessId,Service.accessNome!!,images)

         viewModelScope.launch {
             uiStateProductAdd = try {
                 Service.retrofitService.sendProduct(product)
                 isAlertShow=true
                 UiStateProductAdd.Success

             } catch (e: IOException) {
                 UiStateProductAdd.Error
             }
         }
     }



    /**se il prodotto e' stato salvato correttamente ritorno alla home*/
    fun saveProductSuccess(navController: NavHostController) {
        refresh()
        uiStateProductAdd= UiStateProductAdd.Loading
        immagini=emptyList()
        nomeProdotto=""
        prezzo=""
        isAlertShow=false
        navController.navigate(ScreenApp.Home.name)


    }

    fun canAddProductButton() {
        val a = !nomeProdotto.isBlank()
        val b = prezzo.toDoubleOrNull() != null
        val c = !immagini.isEmpty()
        isAddEnabled = a && b && c
    }





    /**logica vecchia - da rivedere*/
    fun getImages() {
//    avvio operaz. asyn per getImages()
        viewModelScope.launch {
            uiStateProduct = try {
                val listResult = Service.retrofitService.getImages()
                UiStateProduct.Success(resultList = listResult)
            } catch (e: IOException) {
                UiStateProduct.Error
            }
        }
    }

    /**get singolo prodotto forse non necessaria - da rivedere */
    fun getProductDetail(prodottoId:Long){
        uiStateProductDetail= UiStateProductDetail.Loading
        viewModelScope.launch {
            uiStateProductDetail = try {
                val result = Service.retrofitService.getProduct(prodottoId)
                UiStateProductDetail.Success(result = result)
            } catch (e: IOException) {
                UiStateProductDetail.Error
            }
        }

    }

//    fun inputError(): List<ErrorMess> {
//        val errors = mutableListOf<ErrorMess>()
//        if (nomeProdotto.isBlank() ) {
//            errors.add(ErrorMess("Prodotto", "inserire nome prodotto"))
//            isAlertShow=true
//        }
//        if (prezzo.toDoubleOrNull() == null) {
//            errors.add(ErrorMess("Prezzo", "inserire valore valido prezzo"))
//            isAlertShow=true
//        }
//        if (immagini.isEmpty() || immagini.all { it == null }) {
//            errors.add(ErrorMess("Immagine", "inserire un valore di immagine valido"))
//            isAlertShow=true
//        }
//        return errors }
}

private fun convertUriToBase64(contentResolver: ContentResolver, uri: Uri): String? {
    val inputStream = contentResolver.openInputStream(uri)
    val byteArrayOutputStream = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var bytesRead: Int
    while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead)
    }
    val imageBytes = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}