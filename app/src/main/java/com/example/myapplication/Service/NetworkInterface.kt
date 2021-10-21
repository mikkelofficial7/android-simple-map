package com.example.myapplication.service

import com.example.myapplication.Model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NetworkInterface {
    @GET("json")
    fun getLocation(
        @Query("input") keyword: String,
        @Query("language") language: String = "en",
        @Query("key") apikey: String,
    ) : Call<PlaceResponse>
}