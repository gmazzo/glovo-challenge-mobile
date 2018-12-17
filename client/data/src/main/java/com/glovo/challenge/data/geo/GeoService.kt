package com.glovo.challenge.data.geo

import com.google.android.gms.maps.model.LatLng

interface GeoService {

    fun decodePolygons(polygons: List<String>): List<List<LatLng>>

}
