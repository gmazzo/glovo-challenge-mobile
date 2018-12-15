package com.glovo.challenge.cities

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import com.glovo.challenge.R
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

internal class ExplorePresenter @Inject constructor(
    private val view: ExploreContract.View,
    context: Context,
    citiesRepository: CitiesRepository,
    private val locationService: LocationService,
    private val initialData: InitialData?,
    private val markerIcon: Lazy<BitmapDescriptor>
) : ExploreContract.Presenter {

    @ColorInt
    private val workingAreaFillColor = ContextCompat.getColor(context, R.color.working_areas_fill)

    @ColorInt
    private val workingAreaBorderColor = ContextCompat.getColor(context, R.color.working_areas_border)

    private val getCities = Maybe.fromCallable { initialData?.cities }
        .switchIfEmpty(citiesRepository.listCities())
        .subscribeOn(Schedulers.computation())
        .cache()

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
            .subscribe(view::showCities)
    }

    private fun focusOnInitialLocation() {
        if (focusOnInitialLocation) {
            val initialLocation = initialData?.location

            if (initialLocation != null) {
                focusMapOnTarget(
                    LatLng(initialLocation.latitude, initialLocation.longitude), true
                )

            } else {
                view.showCity(null, false)
                view.showChooseCities()
            }
        }
    }

    override fun onMapFocusTarget(target: LatLng) =
        focusMapOnTarget(target, false)

    private fun focusMapOnTarget(target: LatLng, focusInWholeWorkingArea: Boolean) {
        tryFindCityDisposable.dispose()

        tryFindCityDisposable = getCities
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { city -> city.workingBounds.contains(target) }
            .firstElement()
            .observeOn(AndroidSchedulers.mainThread())
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
