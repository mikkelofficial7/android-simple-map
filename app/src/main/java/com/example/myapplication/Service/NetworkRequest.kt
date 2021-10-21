package com.example.myapplication.service

import com.example.myapplication.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkRequest {
    private var retrofit: Retrofit

    init {

        val client = createOkHttpClientWithTrustCertificate()

        retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/autocomplete/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz) as T
    }

    private fun createOkHttpClientWithTrustCertificate(): OkHttpClient? {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }

                }
            )

            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            OkHttpClient
                .Builder()
                .sslSocketFactory(sslContext.socketFactory)
                .build()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}