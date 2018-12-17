package com.glovo.challenge.data.cities

import com.glovo.challenge.data.BaseTest
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import java.util.concurrent.atomic.AtomicInteger

class CitiesRepositoryCacheImplTest : BaseTest() {

    @Mock
    private lateinit var base: CitiesRepository

    private val impl by lazy { CitiesRepositoryCacheImpl(base) }

    @Before
    fun setup() {
        val counter = AtomicInteger()

        `when`(base.listCities()).thenReturn(
            Single.fromCallable {
                listOf(TEST_CITY.copy("city#" + counter.incrementAndGet()))
            })

        `when`(base.getCityDetails(anyString() ?: "")).thenAnswer {
            Maybe.fromCallable {
                TEST_CITY.copy(name = it.arguments[0].toString() + counter.incrementAndGet())
            }
        }
    }

    @Test
    fun testList() {
        val first = impl.listCities().blockingGet()

        assertNotNull(first)
        assertEquals(first, impl.listCities().blockingGet())
        assertEquals(first, impl.listCities().blockingGet())
        assertEquals(first, impl.listCities().blockingGet())

        assertNotEquals(first, base.listCities().blockingGet())
    }

    @Test
    fun testGetDetails() {
        val firstBCN = impl.getCityDetails("BCN").blockingGet()
        val firstBUE = impl.getCityDetails("BUE").blockingGet()

        assertNotNull(firstBCN)
        assertNotNull(firstBUE)
        assertNotEquals(firstBCN, firstBUE)

        assertEquals(firstBCN, impl.getCityDetails("BCN").blockingGet())
        assertEquals(firstBCN, impl.getCityDetails("BCN").blockingGet())
        assertEquals(firstBCN, impl.getCityDetails("BCN").blockingGet())
        assertNotEquals(firstBCN, base.getCityDetails("BCN").blockingGet())

        assertEquals(firstBUE, impl.getCityDetails("BUE").blockingGet())
        assertEquals(firstBUE, impl.getCityDetails("BUE").blockingGet())
        assertEquals(firstBUE, impl.getCityDetails("BUE").blockingGet())
        assertNotEquals(firstBUE, base.getCityDetails("BUE").blockingGet())
    }

    companion object {

        private val TEST_CITY = City(
            code = "aCode",
            name = "aName",
            country = Country(code = "aCode", name = "aName"),
            workingArea = emptyList()
        )

    }

}
