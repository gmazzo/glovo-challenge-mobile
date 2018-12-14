package com.glovo.challenge.cities.details

import com.glovo.challenge.BaseTest
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
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

    private val presenter by lazy { CityDetailsPresenter(view, citiesRepository) }

    @Test
    fun testOnStart() {
        val city = City(code = "aCode", name = "aName", country = Country("aCode", "aName"), workingArea = emptyList())

        `when`(citiesRepository.getCityDetails("TODO")).thenReturn(Maybe.just(city))

        presenter.onStart()

        verify(view).showDetails(city)
    }

    @Test
    fun testOnStop() {
        presenter.getDetailsDisposable = disposable
        presenter.onStop()

        verify(disposable).dispose()
    }

}
