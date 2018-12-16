package com.glovo.challenge.cities

import com.glovo.challenge.data.models.City
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

typealias CityGeoData = Triple<City, MarkerOptions, List<PolygonOptions>>
