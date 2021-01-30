package `in`.phoenix.currencyconverter.home

import `in`.phoenix.currencyconverter.R
import `in`.phoenix.currencyconverter.util.isVisible
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewModelProvider(this as FragmentActivity).get(MainViewModel::class.java)
            .also {
                mainViewModel = it
                subscribeObservers()
                mainViewModel.fetchCurrencyList()
            }

        amIvSwapFromTo.setOnClickListener(clickListener)
        amIvGo.setOnClickListener(clickListener)
    }

    private fun subscribeObservers() {
        mainViewModel.observeCurrencyList.observe(this, { currencyList ->
            currencyList?.let {
                if (currencyList.isEmpty()) {
                    Log.d("MainActivity", "total currencies: ZERO")

                } else {
                    if (mainViewModel.fromCurrency == null) {
                        mainViewModel.fromCurrency = currencyList.find {
                            it.id == "INR"
                        }
                    }

                    if (mainViewModel.toCurrency == null) {
                        mainViewModel.toCurrency = currencyList.find {
                            it.id == "USD"
                        }
                    }
                }
            }
        })

        mainViewModel.observeFromCurrency.observe(this, { fromCurrency ->
            fromCurrency?.let {
                amTvCurrencyNameFrom.text = it.currencyName
                amTvCurrencyFrom.text = it.id
                amTvCurrencyValueFrom.text = "1 ${it.id} is"
                amTvCurrencyValueTo.text = ""
            }
        })

        mainViewModel.observeToCurrency.observe(this, { toCurrency ->
            toCurrency?.let {
                amTvCurrencyNameTo.text = it.currencyName
                amTvCurrencyTo.text = it.id
                amTvCurrencyValueTo.text = ""
            }
        })
    }

    private val clickListener = View.OnClickListener {
        when (it.id) {
            R.id.amIvSwapFromTo -> {
                if (!amPbLoading.isVisible()) {
                    val isSwap = mainViewModel.swapSelectedCurrency()
                    if (!isSwap) {
                        Toast.makeText(MainActivity@ this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.amIvGo -> {
                if (!amPbLoading.isVisible) {
                    if (mainViewModel.checkSanity()) {
                        mainViewModel.getConversion()

                    } else {
                        Toast.makeText(MainActivity@ this, R.string.oops, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}