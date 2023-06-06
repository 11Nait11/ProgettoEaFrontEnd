package com.example.progettoeafrontend.network


import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL =
    "http://192.168.1.6:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface AppService{

    @GET("utente-api/utenti")
    suspend fun getUtenti():List<User>

    @GET("image-api/images")
    suspend fun getImages():List<Image>

    @GET("prodotto-api/prodotti/{idProdotto}")
    suspend fun getProduct(@Path("idProdotto") idProdotto: Long): Product

    @DELETE("prodotto-api/prodotti/{idProdotto}")
    suspend fun deleteProduct(@Path("idProdotto") idProdotto: Long)


}
//interface ImageApiService {
//    @GET("images")
//    suspend fun getImages(@Query("idProdotto") idProdotto: Int): List<Image>
//}
//puoi chiamarlo val images = service.getImages(1)

object Service{
    val retrofitService : AppService by lazy{
        retrofit.create(AppService::class.java)
    }
}