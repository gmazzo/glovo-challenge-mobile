package com.glovo.challenge.cities.details

import androidx.annotation.VisibleForTesting
import com.glovo.challenge.cities.details.CityDetailsModule.Companion.EXTRA_CITY_CODE
import com.glovo.challenge.data.cities.CitiesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import javax.inject.Inject
import javax.inject.Named

internal class CityDetailsPresenter @Inject constructor(
    private val view: CityDetailsContract.View,
    private val citiesRepository: CitiesRepository,
    @Named(EXTRA_CITY_CODE) private val cityCode: String
) : CityDetailsContract.Presenter {

    @VisibleForTesting
    internal var getCityDetailsDisposable = Disposables.disposed()

    override fun onStart() {
        getCityDetailsDisposable.dispose()

        getCityDetailsDisposable = citiesRepository.getCityDetails(cityCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showDetails)
    }

    override fun onStop() {
        getCityDetailsDisposable.dispose()
    }

}
