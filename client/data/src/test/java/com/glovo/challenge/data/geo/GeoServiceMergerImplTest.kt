package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BaseTest
import com.glovo.challenge.data.models.WorkingArea
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.Polygon
import org.mockito.AdditionalMatchers.aryEq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class GeoServiceMergerImplTest(
    private val polygons: WorkingArea
) : BaseTest() {

    @Mock
    internal lateinit var geoService: GeoService

    @Mock
    lateinit var geometryFactory: GeometryFactory

    @Mock
    internal lateinit var linearRing: LinearRing

    @Mock
    internal lateinit var unifiedPolygon: Polygon

    private val impl by lazy { GeoServiceMergerImpl(geoService, geometryFactory) }

    @Test
    fun testDecodePolygons() {
        val dummyEncoded = polygons.map { System.identityHashCode(it).toString(16) }
        val dummyCoords = polygons.map { p ->
            p.map { Coordinate(it.longitude, it.latitude) }.let { it + it[0] }.toTypedArray()
        }
        val dummyPolygons = polygons.map { mock(Polygon::class.java) }
        val dummyMergedCoords = (50..54).map { Coordinate(it.toDouble(), it.toDouble()) }.toTypedArray()
        val dummyMerged = listOf(dummyMergedCoords.map { LatLng(it.y, it.x) })

        `when`(geoService.decodePolygons(dummyEncoded)).thenReturn(polygons)
        `when`(geometryFactory.buildGeometry(dummyPolygons)).thenReturn(unifiedPolygon)
        `when`(unifiedPolygon.union()).thenReturn(unifiedPolygon)
        `when`(unifiedPolygon.isValid).thenReturn(true)
        `when`(unifiedPolygon.exteriorRing).thenReturn(linearRing)
        `when`(linearRing.coordinates).thenReturn(dummyMergedCoords)

        dummyCoords.forEachIndexed { i, it ->
            `when`(geometryFactory.createPolygon(aryEq(it) ?: it)).thenReturn(dummyPolygons[i])
        }

        val result = impl.decodePolygons(dummyEncoded)

        verify(geoService).decodePolygons(dummyEncoded)

        if (dummyEncoded.size >= 2) {
            assertEquals(dummyMerged, result)

            dummyCoords.forEach { verify(geometryFactory, atLeastOnce()).createPolygon(aryEq(it) ?: it) }
            verify(unifiedPolygon).union()
            verify(unifiedPolygon).exteriorRing
            verify(linearRing).coordinates

        } else {
            assertEquals(polygons, result)

            verifyZeroInteractions(geometryFactory)
        }

    }

    companion object {

        private val TEST_POLYGON1 = (1..3).map { it.asPolygon() }

        private val TEST_POLYGON2 = (1..2).map { (it * 20).asPolygon() }

        private fun Int.asPolygon() = LatLng(toDouble(), 10 * toDouble())

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data() = listOf(
            arrayOf(listOf(TEST_POLYGON1)),
            arrayOf(listOf(TEST_POLYGON2)),
            arrayOf(listOf(TEST_POLYGON1, TEST_POLYGON2)),
            arrayOf(listOf(TEST_POLYGON2, TEST_POLYGON1))
        )

    }

}
