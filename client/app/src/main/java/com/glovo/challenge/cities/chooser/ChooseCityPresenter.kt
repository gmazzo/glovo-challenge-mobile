package com.glovo.challenge.cities.chooser

import androidx.annotation.VisibleForTesting
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.models.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import java.util.*
import javax.inject.Inject

internal class ChooseCityPresenter @Inject constructor(
    private val view: ChooseCityContract.View,
    private val citiesRepository: CitiesRepository
) : ChooseCityContract.Presenter {

    @VisibleForTesting
    internal var listCitiesDisposable = Disposables.disposed()

    override fun onStart() {
        listCitiesDisposable.dispose()

        listCitiesDisposable = citiesRepository.listCities()
            .map(::sortAndAddCountries)
            .doOnSuccess {
                System.out.print(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showItems)
    }

    private fun sortAndAddCountries(cities: List<City>): List<ChooseCityContract.Item> = cities
        .groupByTo(
            destination = TreeMap { a, b -> a.name.compareTo(b.name) }, // sorts by country name
            keySelector = { it.country },
            valueTransform = { ChooseCityContract.Item(city = it) }
        )
        .flatMap { listOf(ChooseCityContract.Item(country = it.key)) + it.value }

    override fun onStop() {
        listCitiesDisposable.dispose()
    }

}
