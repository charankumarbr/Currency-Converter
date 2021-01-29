package `in`.phoenix.currencyconverter.home

import `in`.phoenix.currencyconverter.CurrencyConverter
import `in`.phoenix.currencyconverter.model.Currency
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Charan on January 29, 2021
 */
class MainViewModel: ViewModel() {

    private val fromCurrencyMLD = MutableLiveData<Currency>()
    val observeFromCurrency = fromCurrencyMLD as LiveData<Currency>
    var fromCurrency: Currency? = null
        set(value) = fromCurrencyMLD.postValue(value).apply {
            field = value
        }

    private val toCurrencyMLD = MutableLiveData<Currency>()
    val observeToCurrency = toCurrencyMLD as LiveData<Currency>
    var toCurrency: Currency? = null
        set(value) = toCurrencyMLD.postValue(value).apply {
            field = value
        }

    private val currencyList = MutableLiveData<List<Currency>>()
    val observeCurrencyList = currencyList as LiveData<List<Currency>>

    fun fetchCurrencyList() {
        val currencies = currencyList.value
        if (currencies == null || currencies.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val currencies = MainRepo.fetchCurrencyList()
                currencies?.let {
                    currencyList.postValue(it)

                } ?:currencyList.postValue(ArrayList())
            }

        } else {
            currencyList.postValue(currencies)
        }
    }

    fun swapSelectedCurrency(): Boolean {
        return if (fromCurrency != null && toCurrency != null) {
            val temp = toCurrency
            toCurrency = fromCurrency
            fromCurrency = temp
            true
        } else {
            false
        }
    }
}