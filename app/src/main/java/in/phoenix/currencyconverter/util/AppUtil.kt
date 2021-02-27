package `in`.phoenix.currencyconverter.util

/**
 * Created by Charan on February 27, 2021
 */
object AppUtil {

    fun isNewUI(): Boolean {
        val randomValue = (0..10).random()
        return randomValue % 2 == 0
    }
}