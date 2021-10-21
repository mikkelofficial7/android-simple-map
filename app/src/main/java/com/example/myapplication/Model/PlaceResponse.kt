package com.example.myapplication.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlaceResponse (
    @SerializedName("error_message") var errorMessage: String,
    @SerializedName("predictions") var predictions: ArrayList<LocationData>? = arrayListOf(),
    @SerializedName("status") var status: String
) : Parcelable

@Parcelize
class LocationData (
    @SerializedName("description") var description: String,
    @SerializedName("place_id") var placeId: String,
    @SerializedName("reference") var reference: String,
    @SerializedName("types") var types: Array<String>,
) : Parcelable