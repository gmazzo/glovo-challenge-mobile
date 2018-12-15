package com.glovo.challenge.cities

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import com.glovo.challenge.R
import com.glovo.challenge.data.cities.CitiesRepository
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
    context: Context,
    citiesRepository: CitiesRepository,
    private val view: ExploreContract.View,
    private val initialData: InitialData?,
    private val markerIcon: Lazy<BitmapDescriptor>
) : ExploreContract.Presenter {

    @ColorInt
    private val workingAreaFillColor = ContextCompat.getColor(context, R.color.working_areas_fill)

    @ColorInt
    private val workingAreaBorderColor = ContextCompat.getColor(context, R.color.working_areas_border)

    private val getCities = Maybe.fromCallable { initialData?.cities }
        .switchIfEmpty(citiesRepository.listCities())
        .subscribeOn(Schedulers.io())
        .cache()

    @VisibleForTesting
    internal var loadWorkAreasDisposable = Disposables.disposed()

    @VisibleForTesting
    internal var tryFindCityDisposable = Disposables.disposed()

    override fun onMapReady() {
        loadWorkAreas()

        initialData?.location?.let { onMapFocusTarget(LatLng(it.latitude, it.longitude)) }
    }

    private fun loadWorkAreas() {
        loadWorkAreasDisposable.dispose()

        loadWorkAreasDisposable = getCities
            .map { it.map { city -> Triple(city, city.markerOptions, city.workingArea.polygonOptions) } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showCities)
    }

    override fun onMapFocusTarget(target: LatLng) = tryFindCity(target)

    private fun tryFindCity(target: LatLng) {
        tryFindCityDisposable.dispose()

        tryFindCityDisposable = getCities
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { city -> city.workingBounds.contains(target) }
            .firstElement()
            .doOnComplete { view.showCity(null, false) }
            .subscribe { view.showCity(it, false) }
    }

    override fun onStop() {
        loadWorkAreasDisposable.dispose()
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
