package com.glovo.challenge.data.geo

import com.glovo.challenge.data.models.WorkingArea
import javax.inject.Inject
import javax.inject.Named

internal class GeoServiceCacheImpl @Inject constructor(
    @Named("impl") private val impl: GeoService
) : GeoService {

    private val cache = mutableMapOf<List<String>, WorkingArea>()

    override fun decodePolygons(polygons: List<String>) =
        cache.getOrPut(polygons) {
            impl.decodePolygons(polygons)
        }

}
