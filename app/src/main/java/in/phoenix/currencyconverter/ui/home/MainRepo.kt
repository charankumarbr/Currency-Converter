package `in`.phoenix.currencyconverter.ui.home

import `in`.phoenix.currencyconverter.CurrencyConverter
import `in`.phoenix.currencyconverter.model.Currency
import `in`.phoenix.currencyconverter.network.ApiConnector
import `in`.phoenix.currencyconverter.util.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Charan on January 29, 2021
 */
object MainRepo {

    suspend fun fetchCurrencyList(): List<Currency>? {

        return withContext(Dispatchers.IO) {

            var currencies: List<Currency>? = null
            val bufferedReader = BufferedReader(
                InputStreamReader(
                    CurrencyConverter.appContext.assets
                        .open(AppConstants.CURRENCY_LIST_FILE_NAME)
                )
            )

            val builder = StringBuilder()
            var mLine = bufferedReader.readLine()
            while (mLine != null) {
                builder.append(mLine) // process line
                mLine = bufferedReader.readLine()
            }

            bufferedReader.close()

            val jsonArray = JSONArray(builder.toString())
            if (jsonArray.length() > 0) {
                currencies = ArrayList()
                for (index in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(index)
                    val currency = Currency(
                        jsonObject.getString("currencyName"),
                        jsonObject.getString("id")
                    )
                    currencies.add(currency)
                }
            }

            if (null != currencies) {
                Collections.sort(currencies) { o1, o2 -> o1.currencyName.compareTo(o2.currencyName) }
            }

            currencies
        }
    }

    suspend fun getConversion(fromCurrencyId: String, toCurrencyId: String): String {

        return withContext(Dispatchers.IO) {
            ApiConnector.getAppApi().getCurrencyConversion(
                fromCurrencyId + "_" + toCurrencyId,
                ApiConnector.API_KEY
            )
        }
    }
}