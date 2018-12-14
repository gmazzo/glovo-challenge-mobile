package com.glovo.challenge.cities

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import com.glovo.challenge.R
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.location.LocationService
import com.glovo.challenge.data.models.WorkingArea
import com.glovo.challenge.models.InitialData
import com.google.android.gms.maps.model.PolygonOptions
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import javax.inject.Inject

internal class ExplorePresenter @Inject constructor(
    context: Context,
    private val view: ExploreContract.View,
    private val citiesRepository: CitiesRepository,
    private val locationService: LocationService,
    private val initialData: InitialData?
) : ExploreContract.Presenter {

    @ColorInt
    private val workingAreaFillColor = ContextCompat.getColor(context, R.color.working_areas_fill)

    @ColorInt
    private val workingAreaBorderColor = ContextCompat.getColor(context, R.color.working_areas_border)

    @VisibleForTesting
    internal var loadWorkAreasDisposable = Disposables.disposed()

    override fun onMapReady() {
        loadWorkAreas()
    }

    private fun loadWorkAreas() {
        loadWorkAreasDisposable.dispose()

        loadWorkAreasDisposable = Maybe.fromCallable { initialData?.cities }
            .switchIfEmpty(citiesRepository.listCities())
            .map {
                it.flatMap { city -> city.workingArea.polygonOptions.map { poly -> city to poly } }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showWorkAreas)
    }

    override fun onStop() {
        loadWorkAreasDisposable.dispose()
    }

    private val WorkingArea.polygonOptions
        get() = map {
            PolygonOptions().apply {
                fillColor(workingAreaFillColor)
                strokeColor(workingAreaBorderColor)

                addAll(it)
            }
        }

}
