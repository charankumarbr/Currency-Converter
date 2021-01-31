package `in`.phoenix.currencyconverter.adapter

import `in`.phoenix.currencyconverter.R
import `in`.phoenix.currencyconverter.model.Currency
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Charan on January 31, 2021
 */
class CurrencyAdapter(private val currencies: List<Currency>,
                      private val fromId: String?,
                      private val toId: String?,
                      private val currencyClickListener: CurrencyClickListener?)
    : RecyclerView.Adapter<CurrencyItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_currency_item,
            parent, false)
        return CurrencyItemViewHolder(view, currencyClickListener)
    }

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {
        holder.setData(currencies[position], fromId, toId)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }
}