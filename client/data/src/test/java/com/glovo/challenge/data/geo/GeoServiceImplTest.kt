package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BaseTest
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GeoServiceImplTest : BaseTest() {

    private val impl by lazy { GeoServiceImpl() }

    @Test
    fun testDecodePolygons() {
        val expected = listOf(
            listOf(
                LatLng(38.5, -120.2),
                LatLng(40.7, -120.95),
                LatLng(43.252, -126.453)
            )
        )

        val actual = impl.decodePolygons("_p~iF~ps|U_ulLnnqC_mqNvxq`@")

        assertEquals(expected.size, actual.size)
        expected.forEachIndexed { i, itI ->
            val actualI = actual[i]

            assertEquals(itI.size, actualI.size)
            itI.forEachIndexed { j, expectedLatLng ->
                val actualLatLng = actualI[j]

                assertEquals(expectedLatLng.latitude, actualLatLng.latitude, 0.0001)
                assertEquals(expectedLatLng.longitude, actualLatLng.longitude, 0.0001)
            }
        }
    }

}
