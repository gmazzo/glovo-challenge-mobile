package com.glovo.challenge.cities.details

import androidx.annotation.VisibleForTesting
import com.glovo.challenge.cities.details.CityDetailsModule.Companion.EXTRA_CITY
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.models.City
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import javax.inject.Inject
import javax.inject.Named

internal class CityDetailsPresenter @Inject constructor(
    private val view: CityDetailsContract.View,
    private val citiesRepository: CitiesRepository,
    @Named(EXTRA_CITY) private val city: City
) : CityDetailsContract.Presenter {

    @VisibleForTesting
    internal var getCityDetailsDisposable = Disposables.disposed()

    override fun onStart() {
        getCityDetailsDisposable.dispose()

        getCityDetailsDisposable = citiesRepository.getCityDetails(city.code)
            .switchIfEmpty(Single.just(city))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showDetails)
    }

    override fun onStop() {
        getCityDetailsDisposable.dispose()
    }

}
