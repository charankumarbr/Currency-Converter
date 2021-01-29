package `in`.phoenix.currencyconverter

import android.app.Application
import android.content.Context

/**
 * Created by Charan on January 29, 2021
 */
class CurrencyConverter: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}