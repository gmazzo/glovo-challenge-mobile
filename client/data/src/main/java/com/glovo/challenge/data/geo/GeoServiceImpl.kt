package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BuildConfig
import com.google.maps.android.PolyUtil
import javax.inject.Inject

internal class GeoServiceImpl @Inject constructor() : GeoService {

    override fun decodePolygons(polygons: List<String>) = polygons
        .map(PolyUtil::decode)
        .let {
            if (BuildConfig.FLATTERN_WORKAREAS)
                listOf(PolyUtil.simplify(it.flatten(), 5.0 /* meters */))
            else
                it
        }

}
