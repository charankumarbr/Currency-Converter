package `in`.phoenix.currencyconverter.ui.home

import `in`.phoenix.currencyconverter.R
import `in`.phoenix.currencyconverter.adapter.OnCurrencyChoosen
import `in`.phoenix.currencyconverter.databinding.ActivityMainBinding
import `in`.phoenix.currencyconverter.databinding.ActivityMainNewBinding
import `in`.phoenix.currencyconverter.model.ApiResult
import `in`.phoenix.currencyconverter.model.Currency
import `in`.phoenix.currencyconverter.model.CurrencyResponse
import `in`.phoenix.currencyconverter.util.AppConstants
import `in`.phoenix.currencyconverter.util.AppUtil
import `in`.phoenix.currencyconverter.util.gone
import `in`.phoenix.currencyconverter.util.visible
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), OnCurrencyChoosen {

    private lateinit var mainViewModel: MainViewModel

    private var activityMainBinding: ActivityMainBinding? = null
    private var activityMainNewBinding: ActivityMainNewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        if (AppUtil.isNewUI()) {
            activityMainNewBinding = ActivityMainNewBinding.inflate(layoutInflater)
            activityMainBinding = null
            setContentView(activityMainNewBinding!!.root)

        } else {
            activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            activityMainNewBinding = null
            setContentView(activityMainBinding!!.root)
        }

        ViewModelProvider(this as FragmentActivity).get(MainViewModel::class.java)
            .also {
                mainViewModel = it
                subscribeObservers()
                mainViewModel.fetchCurrencyList()
            }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        if (activityMainBinding != null) {
            activityMainBinding!!.amIvSwapFromTo.setOnClickListener(clickListener)
            activityMainBinding!!.amIvGo.setOnClickListener(clickListener)
            activityMainBinding!!.amLayoutFrom.setOnClickListener(clickListener)
            activityMainBinding!!.amLayoutTo.setOnClickListener(clickListener)

        } else {
            activityMainNewBinding!!.amIvSwapFromTo.setOnClickListener(clickListener)
            activityMainNewBinding!!.amIvGo.setOnClickListener(clickListener)
            activityMainNewBinding!!.amLayoutFrom.setOnClickListener(clickListener)
            activityMainNewBinding!!.amLayoutTo.setOnClickListener(clickListener)
        }
    }

    private fun subscribeObservers() {
        mainViewModel.observeCurrencyList.observe(this, { currencyList ->
            currencyList?.let {
                if (currencyList.isEmpty()) {
                    Toast.makeText(this, getString(R.string.no_currency_to_choose), Toast.LENGTH_SHORT).show()
                    finish()

                } else {
                    if (mainViewModel.fromCurrency == null) {
                        mainViewModel.fromCurrency = currencyList.find {
                            it.id == AppConstants.DEFAULT_FROM
                        }
                    }

                    if (mainViewModel.toCurrency == null) {
                        mainViewModel.toCurrency = currencyList.find {
                            it.id == AppConstants.DEFAULT_TO
                        }
                    }
                }
            }
        })

        mainViewModel.observeFromCurrency.observe(this, { fromCurrency ->
            fromCurrency?.let {
                setFromCurrency(it)
            }
        })

        mainViewModel.observeToCurrency.observe(this, { toCurrency ->
            toCurrency?.let {
                setToCurrency(it)
            }
        })

        mainViewModel.observeCurrencyConversion.observe(this, {
            it?.let {
                setCurrencyConvertResult(it)
            }
        })
    }

    private fun setCurrencyConvertResult(it: ApiResult<CurrencyResponse>) {
        when (it) {
            is ApiResult.Success -> {
                if (activityMainBinding != null) {
                    activityMainBinding!!.amPbLoading.gone()
                    activityMainBinding!!.amTvCurrencyValueTo.text = "${it.data.value} ${it.data.id}"
                    activityMainBinding!!.amIvSwapFromTo.visible()
                    activityMainBinding!!.amTvCurrencyValueFrom.text = "1 ${mainViewModel.fromCurrency?.id} is"

                } else {
                    activityMainNewBinding!!.amPbLoading.gone()
                    activityMainNewBinding!!.amTvCurrencyValueTo.text = "${it.data.value} ${it.data.id}"
                    activityMainNewBinding!!.amIvSwapFromTo.visible()
                    activityMainNewBinding!!.amTvCurrencyValueFrom.text = "1 ${mainViewModel.fromCurrency?.id} is"

                    val amount = activityMainNewBinding!!.amEtAmount.text.toString()
                    val computedValue = mainViewModel.computeValue(amount, it.data)
                    if (computedValue != null) {
                        activityMainNewBinding!!.amTvCurrencyValue.text = computedValue
                        activityMainNewBinding!!.amTvCurrencyValue.visible()

                    } else {
                        activityMainNewBinding!!.amTvCurrencyValue.gone()
                    }
                }
            }

            is ApiResult.Failure -> {
                if (activityMainBinding != null) {
                    activityMainBinding!!.amPbLoading.gone()
                    activityMainBinding!!.amIvSwapFromTo.visible()
                    activityMainBinding!!.amTvCurrencyValueTo.text = ""
                    activityMainBinding!!.amTvCurrencyValueFrom.text = ""

                } else {
                    activityMainNewBinding!!.amPbLoading.gone()
                    activityMainNewBinding!!.amIvSwapFromTo.visible()
                    activityMainNewBinding!!.amTvCurrencyValueTo.text = ""
                    activityMainNewBinding!!.amTvCurrencyValueFrom.text = ""
                }

                if (it.message != null) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                } else if (it.throwable != null) {
                    Toast.makeText(this, R.string.oops, Toast.LENGTH_SHORT).show()
                }
            }

            is ApiResult.Loading -> {
                if (activityMainBinding != null) {
                    activityMainBinding!!.amIvSwapFromTo.gone()
                    activityMainBinding!!.amPbLoading.visible()

                } else {
                    activityMainNewBinding!!.amIvSwapFromTo.gone()
                    activityMainNewBinding!!.amPbLoading.visible()
                }
            }
        }
    }

    private fun setFromCurrency(it: Currency) {
        if (activityMainBinding != null) {
            activityMainBinding!!.amTvCurrencyNameFrom.text = it.currencyName
            activityMainBinding!!.amTvCurrencyFrom.text = it.id
            //amTvCurrencyValueFrom.text = "1 ${it.id} is"
            activityMainBinding!!.amTvCurrencyValueFrom.text = ""
            activityMainBinding!!.amTvCurrencyValueTo.text = ""

        } else {
            activityMainNewBinding!!.amTvCurrencyNameFrom.text = it.currencyName
            activityMainNewBinding!!.amTvCurrencyFrom.text = it.id
            //amTvCurrencyValueFrom.text = "1 ${it.id} is"
            activityMainNewBinding!!.amTvCurrencyValueFrom.text = ""
            activityMainNewBinding!!.amTvCurrencyValueTo.text = ""

            activityMainNewBinding!!.amTvCurrencyValue.text = ""
        }
    }

    private fun setToCurrency(it: Currency) {
        if (activityMainBinding != null) {
            activityMainBinding!!.amTvCurrencyNameTo.text = it.currencyName
            activityMainBinding!!.amTvCurrencyTo.text = it.id
            activityMainBinding!!.amTvCurrencyValueTo.text = ""

        } else {
            activityMainNewBinding!!.amTvCurrencyNameTo.text = it.currencyName
            activityMainNewBinding!!.amTvCurrencyTo.text = it.id
            activityMainNewBinding!!.amTvCurrencyValueTo.text = ""
        }
    }

    private val clickListener = View.OnClickListener {
        when (it.id) {
            R.id.amIvSwapFromTo -> {
                if (!isLoading()) {
                    val isSwap = mainViewModel.swapSelectedCurrency()
                    if (!isSwap) {
                        Toast.makeText(this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.amIvGo -> {
                if (!isLoading()) {
                    if (mainViewModel.checkSanity()) {
                        mainViewModel.getConversion()

                    } else {
                        Toast.makeText(this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.amLayoutFrom -> {
                chooseCurrency(mainViewModel.fromCurrency, mainViewModel.toCurrency, true)
            }

            R.id.amLayoutTo -> {
                chooseCurrency(mainViewModel.fromCurrency, mainViewModel.toCurrency, false)
            }
        }
    }

    private fun isLoading(): Boolean {
        return if (activityMainBinding != null) {
            activityMainBinding!!.amPbLoading.isVisible
        } else {
            activityMainNewBinding!!.amPbLoading.isVisible
        }
    }

    private fun chooseCurrency(fromCurrency: Currency?, toCurrency: Currency?, isFrom: Boolean) {
        val fragment = CurrencyChooserFragment.newInstance(
            mainViewModel.getCurrencyList() as ArrayList<Currency>?,
            fromCurrency?.id,
            toCurrency?.id,
            isFrom)
        if (!isFinishing) {
            fragment.showNow(supportFragmentManager, "CURRENCY_CHOOSER")
        }
    }

    override fun onChoosen(currency: Currency, isFrom: Boolean) {
        if (isFrom) {
            mainViewModel.fromCurrency = currency
        } else {
            mainViewModel.toCurrency = currency
        }
    }
}