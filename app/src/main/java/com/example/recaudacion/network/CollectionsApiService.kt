package com.example.recaudacion.network

// Importaciones de las bibliotecas necesarias para interactuar con la API
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

// URL base para todas las peticiones a la API
private const val BASE_URL = "https://condosa-recaudacion-production.up.railway.app/api/v1/"

// Configuración de Retrofit para realizar las solicitudes HTTP
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create()) // Utiliza Moshi para convertir JSON a objetos Kotlin
    .client(OkHttpClient.Builder().build()) // Configura un cliente HTTP básico
    .build()

// Interfaz que define los puntos finales (endpoints) de la API y los tipos de solicitud que se pueden hacer
interface CollectionsApiService {

    // Obtiene la lista de recaudaciones
    @GET("recaudacion")
    suspend fun getCollections(): List<Recaudacion>

    // Obtiene información personal basada en el número de documento
    @GET("cuenta/persona_nroDocumento/{nroDocumento}")
    suspend fun getCuenta(@Path("nroDocumento") nroDocumento: String): InformacionPersonal

    // Obtiene información del predio basada en el número de RUC
    @GET("predio/predio_ruc/{ruc}")
    suspend fun getPredio(@Path("ruc") ruc: String): Predio

    // Obtiene información de mantenimiento de un recibo específico
    @GET("mant_recibo/nro_recibo/{recibo}")
    suspend fun getMantenimientoRecibo(@Path("recibo") recibo: String): MantenimientoRecibo

}

// Objeto que proporciona acceso a la interfaz CollectionsApiService
object CollectionsApi {
    // Inicializa CollectionsApiService utilizando Retrofit
    val retrofitService: CollectionsApiService by lazy {
        retrofit.create(CollectionsApiService::class.java)
    }
}
