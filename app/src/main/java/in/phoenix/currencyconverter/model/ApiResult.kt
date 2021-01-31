package `in`.phoenix.currencyconverter.model

/**
 * Created by Charan on January 30, 2021
 */
sealed class ApiResult<T> {

    class Loading<Nothing>: ApiResult<Nothing>()

    data class Success<T>(val data: T): ApiResult<T>()

    data class Failure<Nothing>(val message: String?,
                                val throwable: Throwable?): ApiResult<Nothing>()
}
