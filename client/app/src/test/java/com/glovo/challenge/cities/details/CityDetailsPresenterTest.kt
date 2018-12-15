package com.glovo.challenge.cities.details

import com.glovo.challenge.BaseTest
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.testCity
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class CityDetailsPresenterTest : BaseTest() {

    @Mock
    internal lateinit var view: CityDetailsContract.View

    @Mock
    lateinit var citiesRepository: CitiesRepository

    @Mock
    internal lateinit var disposable: Disposable

    private val city = testCity.copy(code = "city1")

    private val presenter by lazy { CityDetailsPresenter(view, citiesRepository, city) }

    @Test
    fun testOnStart() {
        `when`(citiesRepository.getCityDetails("TODO")).thenReturn(Maybe.just(city))

        presenter.onStart()

        verify(view).showDetails(city)
    }

    @Test
    fun testOnStop() {
        presenter.getCityDetailsDisposable = disposable
        presenter.onStop()

        verify(disposable).dispose()
    }

}
