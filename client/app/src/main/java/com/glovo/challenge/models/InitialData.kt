package com.glovo.challenge.models

import android.location.Location
import android.os.Parcelable
import com.glovo.challenge.data.models.City
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InitialData(
    val cities: List<City>,
    val location: Location?
) : Parcelable
