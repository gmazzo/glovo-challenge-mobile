package com.glovo.challenge.cities

import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.location.LocationService
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.WorkingArea
import com.glovo.challenge.models.InitialData
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import dagger.Lazy
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

internal class ExplorePresenter @Inject constructor(
    private val view: ExploreContract.View,
    @ColorInt @Named("workingAreaFillColor") private val workingAreaFillColor: Int,
    @ColorInt @Named("workingAreaBorderColor") private val workingAreaBorderColor: Int,
    citiesRepository: CitiesRepository,
    private val locationService: LocationService,
    private val initialData: InitialData?,
    private val markerIcon: Lazy<BitmapDescriptor>
) : ExploreContract.Presenter {

    private val getCities = Maybe.fromCallable { initialData?.cities }
        .switchIfEmpty(citiesRepository.listCities())
        .subscribeOn(Schedulers.computation())

    @VisibleForTesting
    internal var loadWorkAreasDisposable = Disposables.disposed()

    @VisibleForTesting
    internal var tryFindCityDisposable = Disposables.disposed()

    @VisibleForTesting
    internal var getCurrentLocationDisposable = Disposables.disposed()

    private var focusOnInitialLocation: Boolean = true

    override fun setFocusOnInitialLocation(focusOnInitialLocation: Boolean) {
        this.focusOnInitialLocation = focusOnInitialLocation
    }

    override fun onMapReady() {
        loadWorkAreas()
    }

    private fun loadWorkAreas() {
        loadWorkAreasDisposable.dispose()

        loadWorkAreasDisposable = getCities
            .map { it.map { city -> Triple(city, city.markerOptions, city.workingArea.polygonOptions) } }
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate(view::onLoadReady)
            .doAfterTerminate(::focusOnInitialLocation)
            .subscribe(view::showCitiesGeo)
    }

    private fun focusOnInitialLocation() {
        if (focusOnInitialLocation) {
            val target = initialData?.location?.let { LatLng(it.latitude, it.longitude) }

            focusMapOnTarget(target, true, ifNotFound = {
                view.showCity(null, false)
                view.showChooseCities()
            })
        }
    }

    override fun onMapFocusTarget(target: LatLng) =
        focusMapOnTarget(target, false)

    private fun focusMapOnTarget(target: LatLng?, focusInWholeWorkingArea: Boolean, ifNotFound: () -> Unit = {}) {
        tryFindCityDisposable.dispose()

        val findCity =
            if (target != null) getCities
                .flatMapObservable { Observable.fromIterable(it) }
                .filter { city -> city.workingBounds.contains(target) }
                .firstElement()
            else Maybe.empty()

        tryFindCityDisposable = findCity
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(ifNotFound)
            .subscribe { view.showCity(it, focusInWholeWorkingArea) }
    }

    override fun onPickMyLocation() {
        getCurrentLocationDisposable.dispose()

        getCurrentLocationDisposable = locationService.getCurrentLocation()
            .map { LatLng(it.latitude, it.longitude) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { focusMapOnTarget(it, true) }
    }

    override fun onStop() {
        loadWorkAreasDisposable.dispose()
        tryFindCityDisposable.dispose()
        getCurrentLocationDisposable.dispose()
    }

    private val City.markerOptions
        get() = MarkerOptions()
            .title(name)
            .position(workingBounds.center)
            .icon(markerIcon.get())
            .anchor(.5f, 1f)

    private val WorkingArea.polygonOptions
        get() = map {
            PolygonOptions()
                .fillColor(workingAreaFillColor)
                .strokeColor(workingAreaBorderColor)
                .addAll(it)
        }

}
