package com.glovo.challenge.data.geo

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import javax.inject.Inject
import javax.inject.Named

internal class GeoServiceMergerImpl @Inject constructor(
    @Named("base") private val base: GeoService,
    private val geometryFactory: GeometryFactory
) : GeoService {

    override fun decodePolygons(polygons: List<String>): List<List<LatLng>> {
        val base = base.decodePolygons(polygons)

        if (base.size > 1) {
            try {
                return listOf(mergePolygons(base))

            } catch (e: Exception) {
                // best effort, if we face an issue, just return the original one
                Log.e("GeoServiceImpl", "decodePolygons failed: ${e.localizedMessage}")
            }
        }
        return base
    }

    private fun mergePolygons(coords: List<List<LatLng>>): List<LatLng> {
        val polygons = coords.map(::createPolygon)
        val unified = geometryFactory
            .buildGeometry(polygons)
            .union() as Polygon

        if (!unified.isValid) {
            throw IllegalStateException("the polygon is not valid!")
        }
        return unified.exteriorRing.coordinates.map { LatLng(it.y, it.x) }
    }

    private fun createPolygon(coords: List<LatLng>): Polygon {
        val first = coords.first()
        val sanitized = if (first != coords.last()) coords + first else coords

        return geometryFactory.createPolygon(sanitized
            .map { Coordinate(it.longitude, it.latitude) }
            .toTypedArray())
    }

}
