package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

enum class ErrorType { NETWORK, TIMEOUT, UNKNOWN }

data class ErrorResponse(val code: String, val message: String)

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        Result<Nothing>()

    object NetworkError : Result<Nothing>()
}

suspend fun <T : Any> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Result.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = parseErrorBody(throwable)
                    Result.GenericError(code, errorResponse)
                }
                else -> {
                    Result.GenericError(null, null)
                }
            }
        }
    }
}

fun parseErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}

//private const val MESSAGE_KEY = "message"
//private const val ERROR_KEY = "error"
//
//fun getErrorMessage(responseBody: ResponseBody?): String {
//    return try {
//        val jsonObject = JSONObject(responseBody!!.string())
//        when {
//            jsonObject.has(MESSAGE_KEY) -> jsonObject.getString(MESSAGE_KEY)
//            jsonObject.has(ERROR_KEY) -> jsonObject.getString(ERROR_KEY)
//            else -> "Something wrong happened"
//        }
//    } catch (e: Exception) {
//        "Something wrong happened"
//    }
//}