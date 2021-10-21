package com.example.myapplication.service

import com.example.myapplication.Constant.ErrorCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NetworkCall {
    fun <T>process(call: Call<T>, responseCallback: ResponseCallback<T>) {

        val callback = object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                handleResponse(response, responseCallback)
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                responseCallback.onOtherError(throwable.message.toString(), throwable.localizedMessage.toString())
            }
        }

        call.enqueue(callback)
    }

    private fun <T>handleResponse(response: Response<T>, responseCallback: ResponseCallback<T>) {
        val responseBody = response.body() as T

        when(response.code()) {
            ErrorCode.CODE_BAD_REQUEST -> responseCallback.onBadRequest()
            ErrorCode.CODE_HTTP_NOT_FOUND -> responseCallback.onHttpNotFoundError()
            ErrorCode.CODE_FORBIDDEN -> responseCallback.onForbiddenError()
            ErrorCode.CODE_METHOD_NOT_ALLOW -> responseCallback.onMethodNotAllow()
            ErrorCode.CODE_SERVER_ERROR -> responseCallback.onServerError()
            ErrorCode.CODE_BAD_GATEWAY -> responseCallback.onBadGatewayError()
            ErrorCode.CODE_SERVICE_UNAVAILABLE -> responseCallback.onServiceNotFoundError()
            else -> responseCallback.onSuccess(responseBody)
        }
    }

    fun <T>provideRequest(clazz: Class<T>) : T {
        return NetworkRequest.create(clazz)
    }
}