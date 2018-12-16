package com.glovo.challenge.cities

import android.graphics.Color
import android.location.Location
import com.glovo.challenge.BaseTest
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.location.LocationService
import com.glovo.challenge.data.models.City
import com.glovo.challenge.models.InitialData
import com.glovo.challenge.testCity
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.Lazy
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*

class ExplorePresenterTest : BaseTest() {

    @Mock
    internal lateinit var view: ExploreContract.View

    @Mock
    lateinit var citiesRepository: CitiesRepository

    @Mock
    lateinit var locationService: LocationService

    @Mock
    lateinit var markerIcon: IObjectWrapper

    @Captor
    lateinit var geoDataCaptor: ArgumentCaptor<List<CityGeoData>>

    @Mock
    internal lateinit var location: Location

    @Mock
    internal lateinit var disposable1: Disposable

    @Mock
    internal lateinit var disposable2: Disposable

    @Mock
    internal lateinit var disposable3: Disposable

    private var data: InitialData? = null

    private val presenter by lazy {
        ExplorePresenter(
            view, Color.RED, Color.BLUE, citiesRepository, locationService, data, Lazy { BitmapDescriptor(markerIcon) }
        )
    }

    @Before
    fun setup() {
        `when`(citiesRepository.listCities()).thenReturn(Single.just(TEST_CITIES))
        `when`(locationService.getCurrentLocation()).thenReturn(Maybe.just(location))
    }

    @Test
    fun testOnMapReady_WithLocationFound() {
        `when`(location.latitude).thenReturn(TEST_LOCATION_INSIDE.latitude)
        `when`(location.longitude).thenReturn(TEST_LOCATION_INSIDE.longitude)

        testOnMapReady(InitialData(TEST_CITIES, location), expectPickCity = false)
    }

    @Test
    fun testOnMapReady_WithLocationNotFound() {
        `when`(location.latitude).thenReturn(TEST_LOCATION_OUTSIDE.latitude)
        `when`(location.longitude).thenReturn(TEST_LOCATION_OUTSIDE.longitude)

        testOnMapReady(InitialData(TEST_CITIES, location), expectPickCity = true)
    }

    @Test
    fun testOnMapReady_WithoutLocation() {
        testOnMapReady(InitialData(TEST_CITIES, null), expectPickCity = true)
    }

    @Test
    fun testOnMapReady_NoData() {
        testOnMapReady(null, expectPickCity = true)
    }

    private fun testOnMapReady(data: InitialData?, expectPickCity: Boolean) {
        this.data = data
        presenter.onMapReady()

        if (data?.cities == null) {
            verify(citiesRepository).listCities()
        }

        verify(view).showCitiesGeo(geoDataCaptor.capture() ?: emptyList())

        assertEquals(TEST_CITIES, geoDataCaptor.value.map { it.first })

        if (expectPickCity) {
            verify(view).showCity(null, false)
            verify(view).showChooseCities()

        } else {
            verify(view).showCity(TEST_CITY_INSIDE, true)
            verify(view, never()).showChooseCities()
        }
    }

    @Test
    fun testOnMapFocusTarget_Inside() {
        testOnMapFocusTarget(TEST_LOCATION_INSIDE, TEST_CITY_INSIDE)
    }

    @Test
    fun testOnMapFocusTarget_Outside() {
        testOnMapFocusTarget(TEST_LOCATION_OUTSIDE, null)
    }

    private fun testOnMapFocusTarget(target: LatLng, expectedCity: City?) {
        presenter.onMapFocusTarget(target)

        if (expectedCity != null) {
            verify(view).showCity(expectedCity, false)

        } else {
            verifyZeroInteractions(view)
        }
    }

    @Test
    fun testOnPickMyLocation_Inside() {
        testOnPickMyLocation(TEST_LOCATION_INSIDE, TEST_CITY_INSIDE)
    }

    @Test
    fun testOnPickMyLocation_Outside() {
        testOnPickMyLocation(LatLng(-100.0, -100.0), null)
    }

    private fun testOnPickMyLocation(target: LatLng, expectedCity: City?) {
        `when`(location.latitude).thenReturn(target.latitude)
        `when`(location.longitude).thenReturn(target.longitude)

        presenter.onPickMyLocation()

        verify(locationService).getCurrentLocation()

        if (expectedCity != null) {
            verify(view).showCity(expectedCity, true)

        } else {
            verifyZeroInteractions(view)
        }
    }

    @Test
    fun testOnStop() {
        presenter.loadWorkAreasDisposable = disposable1
        presenter.tryFindCityDisposable = disposable2
        presenter.getCurrentLocationDisposable = disposable3
        presenter.onStop()

        verify(disposable1).dispose()
        verify(disposable2).dispose()
        verify(disposable3).dispose()
    }

    companion object {

        private val TEST_BOUNDS = LatLngBounds(LatLng(-10.0, -10.0), LatLng(10.0, 10.0))
        private val TEST_LOCATION_INSIDE = TEST_BOUNDS.center
        private val TEST_LOCATION_OUTSIDE = LatLng(-100.0, -100.0)

        private val TEST_CITY_INSIDE =
            testCity.copy(code = "city2", workingArea = listOf(listOf(TEST_BOUNDS.southwest, TEST_BOUNDS.northeast)))

        private val TEST_CITIES = listOf(
            testCity.copy(code = "city1"),
            TEST_CITY_INSIDE,
            testCity.copy(code = "city3")
        )

    }

}
