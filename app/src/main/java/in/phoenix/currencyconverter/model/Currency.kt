package `in`.phoenix.currencyconverter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Charan on January 29, 2021
 */
@Parcelize
data class Currency(
    val currencyName: String,
    val id: String
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is Currency && other.id == this.id &&
                other.currencyName == this.currencyName) {
            return true
        }
        return super.equals(other)
    }
}
