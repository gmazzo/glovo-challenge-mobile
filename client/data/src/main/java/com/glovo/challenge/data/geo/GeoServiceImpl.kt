package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BuildConfig
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import math.geom2d.Point2D
import math.geom2d.polygon.Polygons2D
import math.geom2d.polygon.SimplePolygon2D
import javax.inject.Inject

internal class GeoServiceImpl @Inject constructor() : GeoService {

    override fun decodePolygons(polygons: List<String>) = polygons
        .map(PolyUtil::decode)
        .let { if (BuildConfig.MERGE_POLYGONS) listOf(mergePolygons(it)) else it }

    private fun mergePolygons(polygons: List<List<LatLng>>) = polygons
        .map { SimplePolygon2D(it.map { coord -> Point2D(coord.longitude, coord.latitude) }) }
        .reduce(Polygons2D::union)
        .let { it.vertices().map { vtx -> LatLng(vtx.y(), vtx.x()) } }

}
