package `in`.phoenix.currencyconverter.adapter

import `in`.phoenix.currencyconverter.model.Currency

/**
 * Created by Charan on January 31, 2021
 */
interface OnCurrencyChoosen {

    fun onChoosen(currency: Currency, isFrom: Boolean)
}