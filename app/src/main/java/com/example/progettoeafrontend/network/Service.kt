package com.example.progettoeafrontend.network



import com.example.progettoeafrontend.model.Image
import com.example.progettoeafrontend.model.Message
import com.example.progettoeafrontend.model.Product
import com.example.progettoeafrontend.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL =
    "http://192.168.1.7:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface AppService{

    @GET("utente-api/utenti")
    suspend fun getUtenti():List<User>
    @GET("image-api/images")
    suspend fun getImages():List<Image>


    /**messaggi*/
    @GET("messaggio-api/messaggi/utente/{idUtente}")
    suspend fun getMessages(@Path("idUtente") idUtente:Long):List<Message>
    @POST("messaggio-api/salva")
    suspend fun saveMessage(@Body m: Message)



    /** prodotti */
    @GET("prodotto-api/prodotti")
    suspend fun getProducts(): List<Product>
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