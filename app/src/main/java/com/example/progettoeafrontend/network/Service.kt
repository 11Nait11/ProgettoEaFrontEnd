package com.example.progettoeafrontend.network

import android.util.Log
import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.model.User
import com.example.progettoeafrontend.screenApp
import com.example.progettoeafrontend.ui.LoginScreen
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Base64

private const val BASE_URL = "http://192.168.1.7:8080/"
private val json = Json { ignoreUnknownKeys = true }
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(TokenInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface AppService {
    @POST("login")
    suspend fun login(@Header("Authorization") credentials64: String): Response<Unit>

    @GET("messaggio-api/messaggi/utente/{idUtente}")
    suspend fun getMessages(@Path("idUtente") idUtente: Long): List<Message>

    @POST("messaggio-api/salva")
    suspend fun saveMessage(@Body m: Message)

    @GET("prodotto-api/prodotti")
    suspend fun getProducts(): List<Product>

    @GET("prodotto-api/prodotti/{idProdotto}")
    suspend fun getProduct(@Path("idProdotto") idProdotto: Long): Product

    @DELETE("prodotto-api/prodotti/{idProdotto}")
    suspend fun deleteProduct(@Path("idProdotto") idProdotto: Long)

    @GET("utente-api/utente/{username}")
    suspend fun getUtente(@Path("username") username :String):User

    @GET("image-api/images")
    suspend fun getImages(): List<Image>
}

object Service {
    val retrofitService: AppService by lazy {
        retrofit.create(AppService::class.java)
    }
    var accessToken: String? = null
    var accessId:Long=0L
    var accessNome:String?=null
}

//evita inserimento header manualemente nelle request
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest: Request = chain.request()
        val accessToken = Service.accessToken
        Log.d("Interceptor", "Chiamo Interceptor: $accessToken")

        // Add the access token to the header
        val requestWithToken: Request = if (accessToken != null) {
            originalRequest.newBuilder()
                .header("Authorization", "$accessToken")
                .build()
        } else {
            originalRequest
        }


            val response: okhttp3.Response = chain.proceed(requestWithToken)
            if(response.code==403){
                Log.e("Interceptor", "scaduto")
//                goToLogin()

            }

            return response

    }
}
