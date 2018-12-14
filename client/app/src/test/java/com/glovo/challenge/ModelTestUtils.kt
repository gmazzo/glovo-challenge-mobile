package com.glovo.challenge

import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country

val testCountry = Country("aCode", "aName")
val testCity = City(code = "city1", name = "aName", country = testCountry, workingArea = listOf("wa1", "wa2"))
