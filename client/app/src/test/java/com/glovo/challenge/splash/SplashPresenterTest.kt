package com.glovo.challenge.splash

import android.location.Location
import com.glovo.challenge.BaseTest
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.location.LocationService
import com.glovo.challenge.models.InitialData
import com.glovo.challenge.testCity
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.net.ConnectException

class SplashPresenterTest : BaseTest() {

    @Mock
    internal lateinit var view: SplashContract.View

    @Mock
    lateinit var citiesRepository: CitiesRepository

    @Mock
    lateinit var locationService: LocationService

    @Mock
    internal lateinit var disposable: Disposable

    private val presenter by lazy { SplashPresenter(view, citiesRepository, locationService) }

    @Test
    fun testOnStart_WithLocation() {
        testOnStart(Location("dummy"))
    }

    @Test
    fun testOnStart_WithoutLocation() {
        testOnStart(null)
    }

    private fun testOnStart(location: Location?) {
        val cities = listOf(
            testCity.copy(code = "city1"),
            testCity.copy(code = "city2"),
            testCity.copy(code = "city3")
        )

        `when`(citiesRepository.listCities()).thenReturn(Single.just(cities))
        `when`(locationService.getCurrentLocation()).thenReturn(Maybe.fromCallable { location })

        presenter.onStart()

        verify(view).onReady(InitialData(cities, location))
    }

    @Test
    fun testOnStart_WithError() {
        val error = ConnectException()

        `when`(citiesRepository.listCities()).thenReturn(Single.error(error))
        `when`(locationService.getCurrentLocation()).thenReturn(Maybe.empty())

        presenter.onStart()

        verify(view).showError(error)
    }

    @Test
    fun testOnStop() {
        presenter.listCitiesDisposable = disposable
        presenter.onStop()

        verify(disposable).dispose()
    }

}
