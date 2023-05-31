package com.example.progettoeafrontend.network


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
    "http://192.168.1.6:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()

interface AppService{

    @GET("utente-api/utenti")
    suspend fun getUtenti():List<Utente>

    @GET("image-api/images")
    suspend fun getImages():List<Image>


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