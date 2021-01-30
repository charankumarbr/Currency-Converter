package `in`.phoenix.currencyconverter.network

import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Charan on January 30, 2021
 */
object ApiConnector {

    const val API_KEY = "YOUR_API_KEY_GOES_HERE"

    private const val BASE_URL = "https://free.currconv.com"

    private const val PRAGMA = "Pragma"

    private const val CACHE_CONTROL = "Cache-Control"

    private var retrofit: Retrofit? = null

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(20L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addNetworkInterceptor(getHttpLoggingInterceptor())
            .addNetworkInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                response.newBuilder()
                    .removeHeader(PRAGMA)
                    .removeHeader(CACHE_CONTROL)
                    .addHeader(CACHE_CONTROL, getCacheControl())
                    .build()
            }.build()
    }

    private fun getHttpLoggingInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun getCacheControl(): String {
        return CacheControl.Builder()
            .maxAge(30, TimeUnit.MINUTES)
            .build()
            .toString()
    }

    fun getAppApi(): AppApi = getRetrofit().create(AppApi::class.java)

}