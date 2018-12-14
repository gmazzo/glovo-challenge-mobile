package com.glovo.challenge

import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
import com.google.android.gms.maps.model.LatLng

val testCountry = Country("aCode", "aName")
val testCity =
    City(code = "city1", name = "aName", country = testCountry, workingArea = listOf(listOf(LatLng(1.1, -1.1))))
