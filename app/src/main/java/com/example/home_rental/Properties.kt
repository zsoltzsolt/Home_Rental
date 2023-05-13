package com.example.home_rental

import android.os.Parcelable
import android.os.Parcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
data class Properties(
    var id: String,
    var title: String,
    var type: String,
    var year: Int,
    var judet: String,
    var city: String,
    var surface: Int,
    var price: Double,
    var money: String,
    var rooms: Int,
    var bath: Int,
    var parking: Boolean,
    var garage: Boolean,
    var airConditioner: Boolean,
    var garden: Boolean,
    var balcon: Boolean,
    var centrala: Boolean,
    var pool: Boolean,
    var internet: Boolean,
    var mobilat: Boolean,
    var description: String,
    var phone: String,
    var image: Array<String>?,
    var firstImage: String,
    var date: String
) : Parcelable


