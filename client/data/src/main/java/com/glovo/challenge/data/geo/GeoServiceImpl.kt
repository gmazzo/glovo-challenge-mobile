package com.glovo.challenge.data.geo

import com.google.maps.android.PolyUtil
import javax.inject.Inject

// TODO implement https://www.geeksforgeeks.org/convex-hull-set-1-jarviss-algorithm-or-wrapping/
internal class GeoServiceImpl @Inject constructor() : GeoService {

    override fun decodePolygons(polygons: List<String>) = polygons
        .map(PolyUtil::decode)

}
