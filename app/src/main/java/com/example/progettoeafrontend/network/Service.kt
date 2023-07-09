package com.example.progettoeafrontend.network

import android.util.Log
import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


//evita di inserire header manualemente nelle request
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest: Request = chain.request()
        val accessToken = Service.accessToken
        Log.d("interceptor", "access token: $accessToken")


        /*** Add the access token to the header*/
        val requestWithToken: Request = if (accessToken != null)
        {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else { originalRequest }


        val response: okhttp3.Response = chain.proceed(requestWithToken)
        return response

    }
}

//private const val BASE_URL = "https://192.168.1.7:8443/"//https
private const val BASE_URL = "http://192.168.1.7:8080/"//http
private val json = Json { ignoreUnknownKeys = true }
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(TokenInterceptor())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

object Service {
    val retrofitService: AppService by lazy {
        retrofit.create(AppService::class.java)
    }
    var accessToken: String? = null
    var refreshToken: String? = null
    var accessId:Long=0L
    var accessNome:String?=null

    suspend fun refreshToken(){
        accessToken="Bearer $refreshToken"
        val headers=retrofitService.sendRefreshToken().headers()
        refreshToken = "Bearer "+headers.get("refresh_token")
        accessToken = "Bearer "+headers.get("access_token")
        Log.d("refreshToken ","Nuovo accessToken: $accessToken")

    }
}



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

    @GET("utente-api/refreshToken")
    suspend fun sendRefreshToken():Response<Unit>

    @GET("image-api/images")
    suspend fun getImages(): List<Image>

    @POST("utente-api/salva")
    suspend fun sendRegister(@Body user: User)

    @POST("prodotto-api/salva")
    suspend fun sendProduct(@Body p: Product)
}



