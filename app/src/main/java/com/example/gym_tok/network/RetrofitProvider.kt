package com.example.gym_tok.network

import com.example.gym_tok.api.ApiService // Importación explícita de TU ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ===============================================================
 * PROVEEDOR DE INSTANCIA DE RETROFIT (ApiClient)
 * ===============================================================
 *
 * Propósito:
 * Este objeto (`object`) actúa como un Singleton. Su misión es crear, configurar
 * y proveer una única instancia del cliente Retrofit y del ApiService para toda la app.
 * Esto asegura que no creemos múltiples objetos de red innecesariamente, ahorrando memoria y recursos.
 */
object RetrofitProvider {

    // URL base de tu backend.
    // La IP "10.0.2.2" es una dirección especial que el emulador de Android utiliza
    // para conectarse al "localhost" (127.0.0.1) de la máquina donde se está ejecutando.
    // Si tu backend corre en el puerto 8080, esta es la IP correcta para que el emulador lo encuentre.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Creación del cliente OkHttp. OkHttp es la librería que Retrofit usa por debajo
    // para realizar las llamadas HTTP.
    private val okHttpClient = OkHttpClient.Builder()
        // Añadimos un interceptor de logging. Esto es EXTREMADAMENTE útil para depurar.
        // Imprimirá en el Logcat (consola) todas las peticiones que salen y las respuestas que llegan,
        // incluyendo headers y el cuerpo (body) del JSON.
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Creación de la instancia de Retrofit.
    // `by lazy` significa que el código dentro de las llaves solo se ejecutará
    // la PRIMERA vez que se acceda a la variable `retrofit`. En accesos posteriores,
    // se devolverá la instancia ya creada. Es una forma eficiente de crear Singletons.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usamos el cliente OkHttp que configuramos arriba.
            // Le decimos a Retrofit que use GsonConverterFactory para convertir
            // las respuestas JSON en nuestras data classes de Kotlin.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Propiedad pública para acceder al servicio de la API.
     * Al igual que `retrofit`, se inicializa de forma perezosa (`lazy`).
     * Llama a `retrofit.create(ApiService::class.java)` para que Retrofit
     * implemente nuestra interfaz `ApiService` y nos dé un objeto listo para usar.
     */
    // CORRECCIÓN: Se especifica el tipo `ApiService` correcto, no el de Firebase.
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}