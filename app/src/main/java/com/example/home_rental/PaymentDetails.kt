package com.example.home_rental

import android.os.Parcelable
import android.os.Parcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
data class PaymentDetails(
    var ownerID: String,
    var propertyId: String,
    var title: String,
    var months: Int,
    var price: String
): Parcelable
