package `in`.phoenix.currencyconverter.ui.home

import `in`.phoenix.currencyconverter.model.ApiResult
import `in`.phoenix.currencyconverter.model.Currency
import `in`.phoenix.currencyconverter.model.CurrencyResponse
import `in`.phoenix.currencyconverter.network.ApiConnector
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

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

    fun checkSanity(): Boolean {
        return if (fromCurrency != null && toCurrency != null &&
            fromCurrency != toCurrency) {
            return true
        } else {
            false
        }
    }

    private val _currencyConversion = MutableLiveData<ApiResult<CurrencyResponse>>()
    val observeCurrencyConversion = _currencyConversion as LiveData<ApiResult<CurrencyResponse>>

    @UiThread
    fun getConversion() {

        if (ApiConnector.API_KEY == "YOUR_API_KEY_GOES_HERE") {
            _currencyConversion.postValue(ApiResult.Failure("REGISTER FOR AN API KEY", null))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _currencyConversion.postValue(ApiResult.Loading())
            try {
                val data = MainRepo.getConversion(fromCurrency!!.id, toCurrency!!.id)
                val jsonObject = JSONObject(data)
                if (fromCurrency != null && toCurrency != null) {
                    val key2Check = "${fromCurrency!!.id}_${toCurrency!!.id}"
                    if (jsonObject.has(key2Check)) {
                        val currencyResponse = CurrencyResponse(key2Check,
                            "${jsonObject.getDouble(key2Check)} ${toCurrency!!.id}")
                        _currencyConversion.postValue(ApiResult.Success(currencyResponse))

                    } else {
                        _currencyConversion.postValue(ApiResult.Failure(null, null))
                    }

                } else {
                    _currencyConversion.postValue(ApiResult.Failure(null, null))
                }

            } catch (exception: Exception) {
                exception.printStackTrace()
                _currencyConversion.postValue(ApiResult.Failure("Oops!", exception))
            }
        }
    }
}