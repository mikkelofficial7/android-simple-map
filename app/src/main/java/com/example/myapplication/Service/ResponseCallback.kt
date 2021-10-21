package com.example.myapplication.service

interface ResponseCallback<T>{

    fun onSuccess(responseBody: T) = Unit

    fun onBadRequest() = Unit

    fun onHttpNotFoundError() = Unit

    fun onForbiddenError() = Unit

    fun onMethodNotAllow() = Unit

    fun onBadGatewayError() = Unit

    fun onServiceNotFoundError() = Unit

    fun onServerError() = Unit

    fun onOtherError(error: String, message: String) = Unit
}