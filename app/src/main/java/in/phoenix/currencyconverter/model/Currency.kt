package `in`.phoenix.currencyconverter.model

/**
 * Created by Charan on January 29, 2021
 */
data class Currency(
    val currencyName: String,
    val id: String
) {

    override fun equals(other: Any?): Boolean {
        if (other is Currency && other.id == this.id &&
                other.currencyName == this.currencyName) {
            return true
        }
        return super.equals(other)
    }
}
