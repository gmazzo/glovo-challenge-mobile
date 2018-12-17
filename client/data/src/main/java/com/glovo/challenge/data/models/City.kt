package com.glovo.challenge.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val code: String,
    val name: String,
    val country: Country,
    val currency: String? = null,
    val timeZone: String? = null,
    val workingArea: WorkingArea
) : Parcelable {

    @IgnoredOnParcel
    val workingBounds by lazy {
        workingArea
            .flatten()
            .let { it.drop(1).fold(LatLngBounds(it[0], it[0]), LatLngBounds::including) }!!
    }

}
