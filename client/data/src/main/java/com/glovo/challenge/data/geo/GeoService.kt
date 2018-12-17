package com.glovo.challenge.data.geo

import com.glovo.challenge.data.models.WorkingArea

interface GeoService {

    fun decodePolygons(polygons: List<String>): WorkingArea

}
