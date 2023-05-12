package com.example.home_rental

import com.google.firebase.storage.StorageReference

data class Properties(
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
    var image: StorageReference?
)
