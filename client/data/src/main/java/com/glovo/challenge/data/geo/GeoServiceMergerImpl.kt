package com.glovo.challenge.data.geo

import android.util.Log
import com.glovo.challenge.data.models.WorkingArea
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

    override fun decodePolygons(polygons: List<String>): WorkingArea {
        val base = base.decodePolygons(polygons)

        if (base.size > 1) {
            try {
                return listOf(mergePolygons(base))

            } catch (e: Exception) {
                // best effort, if we face an issue, just return the original one
                Log.e("GeoServiceMergerImpl", "decodePolygons failed: ${e.localizedMessage}")
            }
        }
        return base
    }

    private fun mergePolygons(coords: WorkingArea): List<LatLng> {
        val polygons = coords.map(::createPolygon)
        val unified = geometryFactory
            .buildGeometry(polygons)
            .union()

        if (!unified.isValid) {
            throw IllegalStateException("the polygon is not valid!")
        }

        val geo = if (unified is Polygon) unified.exteriorRing else unified
        return geo.coordinates.map { LatLng(it.y, it.x) }
    }

    private fun createPolygon(coords: List<LatLng>): Polygon {
        val first = coords.first()
        val sanitized = if (first != coords.last()) coords + first else coords

        return geometryFactory.createPolygon(sanitized
            .map { Coordinate(it.longitude, it.latitude) }
            .toTypedArray())
    }

}
