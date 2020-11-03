package today.kinema.data.api

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

private const val CINEMA_URL_PRODUCTION = "https://kinema.today/api/"
private const val CINEMA_URL_DEVELOPMENT = "http://192.168.1.10:8000/api/"

class KinemaApi @Inject constructor(
    gson: Gson
) {

//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        .build() //Added interceptor to print level body request/response Retrofit

    val service: Retrofit = Retrofit.Builder()
        .baseUrl(CINEMA_URL_PRODUCTION)
        .addConverterFactory(GsonConverterFactory.create(gson))
//        .client(okHttpClient)
        .build()

}