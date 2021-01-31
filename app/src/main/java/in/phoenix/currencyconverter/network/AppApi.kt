package `in`.phoenix.currencyconverter.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Charan on January 30, 2021
 */
interface AppApi {

    @GET("/api/v7/convert?compact=ultra")
    suspend fun getCurrencyConversion(@Query("q") fromToCurrency: String,
                                      @Query("apiKey") apiKey: String): String
}