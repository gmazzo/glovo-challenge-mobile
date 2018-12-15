package com.glovo.challenge.cities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.glovo.challenge.R
import com.glovo.challenge.cities.ExploreModule.Companion.EXTRA_INITIAL_DATA
import com.glovo.challenge.cities.details.CityDetailsFragment
import com.glovo.challenge.data.models.City
import com.glovo.challenge.models.InitialData
import com.glovo.utils.hasLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ExploreActivity : DaggerAppCompatActivity(), ExploreContract.View {

    @Inject
    internal lateinit var presenter: ExploreContract.Presenter

    private lateinit var googleMap: GoogleMap

    private val citiesMarkersAndAreas = mutableListOf<Pair<Marker, List<Polygon>>>()

    private var currentCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_explore)
    }

    override fun onStart() {
        super.onStart()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(::onMapReady)
    }

    @SuppressLint("MissingPermission")
    private fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap.apply {
            isMyLocationEnabled = hasLocationPermission
            setOnMapClickListener(presenter::onMapFocusTarget)
            setOnCameraMoveListener(::onCameraMove)
            setOnMarkerClickListener(::onMarkerClick)
        }

        presenter.onMapReady()
    }

    private fun onCameraMove() {
        presenter.onMapFocusTarget(googleMap.cameraPosition.target)
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        showCity(city = marker.tag as City, focusInWholeWorkingArea = true)
        return true
    }

    private fun clearCities() {
        citiesMarkersAndAreas.forEach { (a, b) ->
            a.remove()
            b.forEach(Polygon::remove)
        }
    }

    override fun showCities(workingAreas: List<Triple<City, MarkerOptions, List<PolygonOptions>>>) {
        clearCities()

        workingAreas.forEach { (city, markerOpt, poliOpts) ->
            val marker = googleMap.addMarker(markerOpt).apply { tag = city }
            val polygons = poliOpts.map(googleMap::addPolygon)

            citiesMarkersAndAreas.add(marker to polygons)
        }
    }

    override fun showCity(city: City, focusInWholeWorkingArea: Boolean) {
        if (city != currentCity) {
            currentCity = city

            if (focusInWholeWorkingArea) {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        city.workingBounds,
                        resources.getDimensionPixelOffset(R.dimen.working_areas_padding)
                    )
                )
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.cityDetails, CityDetailsFragment.newInstance(city.code))
                .commitNow()
        }
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

    companion object {

        fun makeIntent(context: Context, initialData: InitialData?) =
            Intent(context, ExploreActivity::class.java).apply {
                putExtra(EXTRA_INITIAL_DATA, initialData)
            }

    }

}
