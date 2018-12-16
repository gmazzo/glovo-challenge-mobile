package com.glovo.challenge.data.geo

import com.google.maps.android.PolyUtil
import javax.inject.Inject

internal class GeoServiceImpl @Inject constructor() : GeoService {

    override fun decodePolygons(polygons: List<String>) = polygons
        .filter(CharSequence::isNotBlank) // there is empty data!
        .map(PolyUtil::decode)
        .filter { it.size >= 3 } // removes invalid polygons, there are some with less than 3 points!

}
