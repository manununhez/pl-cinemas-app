package today.kinema.data.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import today.kinema.util.LiveDataCallAdapterFactory
import javax.inject.Inject

private const val CINEMA_URL_PRODUCTION = "https://kinema.today/api/"
private const val CINEMA_URL_DEVELOPMENT = "http://192.168.1.10:8000/api/"

class KinemaApi @Inject constructor(
    gson: Gson
) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build() //Added interceptor to print level body request/response Retrofit

    val service = Retrofit.Builder()
        .baseUrl(CINEMA_URL_DEVELOPMENT)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .client(okHttpClient)
        .build()

}