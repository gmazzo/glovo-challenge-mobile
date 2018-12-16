package com.glovo.challenge.splash

import android.location.Location
import androidx.annotation.VisibleForTesting
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.location.LocationService
import com.glovo.challenge.models.InitialData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import javax.inject.Inject

internal class SplashPresenter @Inject constructor(
    private val view: SplashContract.View,
    private val citiesRepository: CitiesRepository,
    private val locationService: LocationService
) : SplashContract.Presenter {

    @VisibleForTesting
    internal var listCitiesDisposable = Disposables.disposed()

    override fun onStart() {
        listCitiesDisposable.dispose()

        listCitiesDisposable = citiesRepository.listCities()
            .zipWith(
                locationService.getCurrentLocation().toSingle(MissingLocation()),
                BiFunction(::InitialData)
            )
            .map { if (it.location is MissingLocation) it.copy(location = null) else it }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::onReady, view::showError)
    }

    override fun onStop() {
        listCitiesDisposable.dispose()
    }

    private class MissingLocation : Location("missing")

}
