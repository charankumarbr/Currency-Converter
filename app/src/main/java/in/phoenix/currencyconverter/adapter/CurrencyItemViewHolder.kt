package `in`.phoenix.currencyconverter.adapter

import `in`.phoenix.currencyconverter.R
import `in`.phoenix.currencyconverter.model.Currency
import `in`.phoenix.currencyconverter.util.gone
import `in`.phoenix.currencyconverter.util.visible
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Charan on January 31, 2021
 */
class CurrencyItemViewHolder(
    private val view: View,
    private val currencyClickListener: CurrencyClickListener?)
    : RecyclerView.ViewHolder(view) {

    private val tvCurrencyName: TextView = view.findViewById(R.id.lciTvName)
    private val tvCurrencyId: TextView = view.findViewById(R.id.lciTvId)
    private val tvSelection: TextView = view.findViewById(R.id.lciTvSelection)

    private lateinit var currency: Currency
    private var fromId: String? = null
    private var toId: String? = null

    init {
        view.setOnClickListener {
            if (this::currency.isInitialized) {
                if (canCurrencyBeChoosen()) {
                    currencyClickListener?.onCurrencyClick(adapterPosition, currency)
                }
            }
        }
    }

    private fun canCurrencyBeChoosen(): Boolean {
        return (currency.id != null && (currency.id != fromId && currency.id != toId))
    }

    fun setData(currency: Currency, fromId: String?, toId: String?) {
        this.currency = currency
        this.fromId = fromId
        this.toId = toId

        tvCurrencyName.text = currency.currencyName
        tvCurrencyId.text = currency.id

        tvSelection.gone()
        if (currency.id == fromId) {
            tvSelection.setText(R.string.from)
            tvSelection.visible()

        } else if (currency.id == toId) {
            tvSelection.setText(R.string.to)
            tvSelection.visible()
        }
    }

}