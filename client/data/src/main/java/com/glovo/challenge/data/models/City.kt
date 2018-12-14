package com.glovo.challenge.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val code: String,
    val name: String,
    val country: Country,
    val workingArea: WorkingArea
) : Parcelable
