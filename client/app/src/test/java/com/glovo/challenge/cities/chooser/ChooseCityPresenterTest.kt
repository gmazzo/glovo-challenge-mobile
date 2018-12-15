package com.glovo.challenge.cities.chooser

import com.glovo.challenge.BaseTest
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.testCity
import com.glovo.challenge.testCountry
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ChooseCityPresenterTest : BaseTest() {

    @Mock
    internal lateinit var view: ChooseCityContract.View

    @Mock
    lateinit var citiesRepository: CitiesRepository

    @Mock
    internal lateinit var disposable: Disposable

    private val presenter by lazy { ChooseCityPresenter(view, citiesRepository) }

    @Test
    fun testOnStart() {
        val country1 = testCountry.copy(name = "AAA")
        val country2 = testCountry.copy(name = "BBB")
        val country3 = testCountry.copy(name = "CCC")
        val city1 = testCity.copy(name = "city1", country = country3)
        val city2 = testCity.copy(name = "city2", country = country1)
        val city3 = testCity.copy(name = "city3", country = country2)
        val city4 = testCity.copy(name = "city4", country = country2)
        val city5 = testCity.copy(name = "city5", country = country1)
        val city6 = testCity.copy(name = "city6", country = country1)

        `when`(citiesRepository.listCities()).thenReturn(Single.just(listOf(city1, city2, city3, city4, city5, city6)))

        presenter.onStart()

        verify(view).showItems(
            listOf(
                ChooseCityContract.Item(country = country1),
                ChooseCityContract.Item(city = city2),
                ChooseCityContract.Item(city = city5),
                ChooseCityContract.Item(city = city6),
                ChooseCityContract.Item(country = country2),
                ChooseCityContract.Item(city = city3),
                ChooseCityContract.Item(city = city4),
                ChooseCityContract.Item(country = country3),
                ChooseCityContract.Item(city = city1)
            )
        )
    }

    @Test
    fun testOnStop() {
        presenter.listCitiesDisposable = disposable
        presenter.onStop()

        verify(disposable).dispose()
    }

}
