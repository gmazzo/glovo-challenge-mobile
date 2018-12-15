package com.glovo.challenge.cities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.glovo.challenge.R
import com.glovo.challenge.cities.ExploreModule.Companion.EXTRA_INITIAL_DATA
import com.glovo.challenge.cities.details.CityDetailsFragment
import com.glovo.challenge.cities.details.NoCityDetailsFragment
import com.glovo.challenge.data.models.City
import com.glovo.challenge.models.InitialData
import com.glovo.utils.hasLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ExploreActivity : DaggerAppCompatActivity(), ExploreContract.View {

    @Inject
    internal lateinit var presenter: ExploreContract.Presenter

    private lateinit var googleMap: GoogleMap

    private val citiesGeo = mutableMapOf<City, CityGeo>()

    private val currentCityGeo = mutableListOf<Polygon>()

    private var currentCity: City? = null

    private val currentGeo get() = currentCity?.let { citiesGeo[it] }

    private var showMarkers: Boolean = true
        set(value) {
            if (field != value) {
                field = value

                updateGeoVisibility()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_explore)

        presenter.setFocusOnInitialLocation(savedInstanceState == null)

        @Suppress("CAST_NEVER_SUCCEEDS") // it will, just yelling because AndroidX
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(::onMapReady)
    }

    private fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        presenter.onMapReady()
    }

    @SuppressLint("MissingPermission")
    override fun onLoadReady() {
        googleMap.apply {
            isMyLocationEnabled = hasLocationPermission
            setOnMapClickListener(::onMapClick)
            setOnCameraMoveListener(::onCameraMove)
            setOnCameraIdleListener(::onCameraIdle)
            setOnMarkerClickListener(::onMarkerClick)
        }
    }

    private fun onCameraMove() {
        // checks if we need to show or hide the markers
        showMarkers = googleMap.cameraPosition.zoom < 8
    }

    private fun onMapClick(latLng: LatLng) {
        // tries to focus on a city by click in a point in the map
        presenter.onMapFocusTarget(latLng, false)
    }

    private fun onCameraIdle() {
        // tries to focus on a city that is in the center of the map
        presenter.onMapFocusTarget(googleMap.cameraPosition.target, true)
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        // focus in the city whose marker has been clicked
        showCity(marker.city, true)
        return true
    }

    private fun updateGeoVisibility() {
        val current = currentGeo?.marker
        val shouldShow = showMarkers

        citiesGeo.values.forEach { it.marker.isVisible = (shouldShow || it.marker != current) }
        currentCityGeo.forEach { it.isVisible = !shouldShow }
    }

    private fun clearCitiesGeo() {
        citiesGeo.values.map(CityGeo::marker).forEach(Marker::remove)
        citiesGeo.clear()
    }

    override fun showCities(citiesGeoData: List<Triple<City, MarkerOptions, List<PolygonOptions>>>) {
        clearCitiesGeo()

        citiesGeoData.forEach { (city, markerOpt, poliOpts) ->
            val marker = googleMap.addMarker(markerOpt).apply { tag = city }

            citiesGeo[city] = CityGeo(marker, poliOpts)
        }
    }

    private fun clearCurrentCityGeo() {
        currentCityGeo.forEach(Polygon::remove)
        currentCityGeo.clear()
    }

    override fun showCity(city: City?, focusInWholeWorkingArea: Boolean) {
        if (city != currentCity) {
            // we switch back on the previous marker visibility before changing the current one
            currentGeo?.marker?.isVisible = true

            currentCity = city

            // updates the current working areas
            clearCurrentCityGeo()
            if (city != null) {
                val current = currentGeo!!

                current.marker.isVisible = false
                current.polygons
                    .map(googleMap::addPolygon)
                    .forEach {
                        it.tag = city

                        currentCityGeo.add(it)
                    }
            }

            // updated the info panel
            val fragment: Fragment
            if (city != null) {
                fragment = CityDetailsFragment.newInstance(city)

                if (focusInWholeWorkingArea) {
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            city.workingBounds,
                            resources.getDimensionPixelOffset(R.dimen.working_areas_padding)
                        )
                    )
                }

            } else {
                fragment = NoCityDetailsFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.cityDetails, fragment)
                .commitNow()
        }
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

    private val Marker.city get() = tag as City

    companion object {

        fun makeIntent(context: Context, initialData: InitialData?) =
            Intent(context, ExploreActivity::class.java).apply {
                putExtra(EXTRA_INITIAL_DATA, initialData)
            }

    }

    private data class CityGeo(
        val marker: Marker,
        val polygons: List<PolygonOptions>
    )

}
