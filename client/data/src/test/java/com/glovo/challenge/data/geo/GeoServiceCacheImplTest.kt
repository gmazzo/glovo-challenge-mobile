package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BaseTest
import com.google.android.gms.maps.model.LatLng
import com.google.common.util.concurrent.AtomicDouble
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any

class GeoServiceCacheImplTest : BaseTest() {

    @Mock
    private lateinit var base: GeoService

    private val impl by lazy { GeoServiceCacheImpl(base) }

    @Before
    fun setup() {
        val counter = AtomicDouble()

        `when`(base.decodePolygons(any() ?: emptyList())).thenAnswer {
            val value = counter.addAndGet(1.0)

            listOf(listOf(LatLng(value, value)))
        }
    }

    @Test
    fun testDecodePolygons() {
        val input1 = listOf("AAA", "BBB")
        val input2 = listOf("CCC")

        val first1 = impl.decodePolygons(input1)
        val first2 = impl.decodePolygons(input2)

        assertNotNull(first1)
        assertNotNull(first2)
        assertNotEquals(first1, first2)

        assertEquals(first1, impl.decodePolygons(input1))
        assertEquals(first1, impl.decodePolygons(input1))
        assertEquals(first1, impl.decodePolygons(input1))
        assertNotEquals(first1, base.decodePolygons(input1))

        assertEquals(first2, impl.decodePolygons(input2))
        assertEquals(first2, impl.decodePolygons(input2))
        assertEquals(first2, impl.decodePolygons(input2))
        assertNotEquals(first2, base.decodePolygons(input2))
    }

}
