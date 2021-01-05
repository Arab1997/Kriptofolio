package com.baruckis.kriptofolio.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

/**
 * Common class used by API responses. ApiResponse is a simple wrapper around the Retrofit2.Call
 * class that convert responses to instances of LiveData.
 * @param <CoinMarketCapType> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<CoinMarketCapType> {
    companion object {
        fun <CoinMarketCapType> create(error: Throwable): ApiErrorResponse<CoinMarketCapType> {
            return ApiErrorResponse(error.message ?: "Unknown error.")
        }

        fun <CoinMarketCapType> create(response: Response<CoinMarketCapType>): ApiResponse<CoinMarketCapType> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body = body)
                }
            } else {

                var errorMsg: String? = null

                if (response.errorBody()?.contentType()?.subtype.equals("json")) {

                    errorMsg = try {

                        // Convert error response to JSON object.
                        val gson = Gson()
                        val type = object : TypeToken<CoinMarketCap<CoinMarketCapType>>() {}.type
                        val errorResponse: CoinMarketCap<CoinMarketCapType> = gson.fromJson(response.errorBody()!!.charStream(), type)

                        errorResponse.status?.errorMessage ?: errorResponse.message

                    } catch (e: Exception) {
                        ""
                    }

                }

                if (errorMsg.isNullOrEmpty()) {
                    val msg = response.errorBody()?.string()
                    errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                }

                ApiErrorResponse(errorMsg ?: "Unknown error.")
            }
        }
    }
}

/**
 * Separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<CoinMarketCapType> : ApiResponse<CoinMarketCapType>()

data class ApiSuccessResponse<CoinMarketCapType>(val body: CoinMarketCapType) : ApiResponse<CoinMarketCapType>()

data class ApiErrorResponse<CoinMarketCapType>(val errorMessage: String) : ApiResponse<CoinMarketCapType>()