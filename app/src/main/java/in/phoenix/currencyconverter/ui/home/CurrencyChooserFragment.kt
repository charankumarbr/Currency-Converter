package `in`.phoenix.currencyconverter.ui.home

import `in`.phoenix.currencyconverter.R
import `in`.phoenix.currencyconverter.adapter.CurrencyAdapter
import `in`.phoenix.currencyconverter.adapter.CurrencyClickListener
import `in`.phoenix.currencyconverter.adapter.OnCurrencyChoosen
import `in`.phoenix.currencyconverter.model.Currency
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 * Use the [CurrencyChooserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrencyChooserFragment : DialogFragment() {

    private var currencies: ArrayList<Currency>? = null
    private var param1: String? = null
    private var param2: String? = null
    private var isFrom: Boolean = false

    private var onCurrencyChoosen: OnCurrencyChoosen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currencies = it.getParcelableArrayList(ARG_CURRENCY_LIST)
            param1 = it.getString(ARG_FROM_CURRENCY)
            param2 = it.getString(ARG_TO_CURRENCY)
            isFrom = it.getBoolean(ARG_IS_FROM)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCurrencyChoosen) {
            onCurrencyChoosen = context
        }
    }

    override fun onResume() {
        super.onResume()
        val window: Window? = dialog?.window
        val size = Point()
        val display: Display? = window?.windowManager?.defaultDisplay
        display?.let {
            it.getSize(size)
            window.setLayout((size.x * 0.90).toInt(), (size.y * 0.90).toInt())
            window.setGravity(Gravity.CENTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_chooser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencies?.let {
            val fccRvCurrency = view.findViewById<RecyclerView>(R.id.fccRvCurrency)
            fccRvCurrency.adapter = CurrencyAdapter(it, param1, param2, currencyClickListener)
        }
    }

    private val currencyClickListener = object : CurrencyClickListener {
        override fun onCurrencyClick(position: Int, currency: Currency) {
            onCurrencyChoosen?.onChoosen(currency, isFrom)
            dismissAllowingStateLoss()
        }
    }

    companion object {

        private const val ARG_CURRENCY_LIST = "currencies"
        private const val ARG_FROM_CURRENCY = "fromCurrency"
        private const val ARG_TO_CURRENCY = "toCurrency"
        private const val ARG_IS_FROM = "isFrom"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param fromCurrency Parameter 1.
         * @param toCurrency Parameter 2.
         * @param isFrom
         * @return A new instance of fragment CurrencyChooserFragment.
         */
        @JvmStatic
        fun newInstance(
            currencies: ArrayList<Currency>?,
            fromCurrency: String?,
            toCurrency: String?,
            isFrom: Boolean
        ) = CurrencyChooserFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_CURRENCY_LIST, currencies)
                    putString(ARG_FROM_CURRENCY, fromCurrency)
                    putString(ARG_TO_CURRENCY, toCurrency)
                    putBoolean(ARG_IS_FROM, isFrom)
                }
            }
    }
}