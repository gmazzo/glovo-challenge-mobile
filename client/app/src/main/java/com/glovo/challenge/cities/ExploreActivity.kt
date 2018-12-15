package com.glovo.challenge.cities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.math.absoluteValue

class ExploreActivity : DaggerAppCompatActivity(), ExploreContract.View {

    @Inject
    internal lateinit var presenter: ExploreContract.Presenter

    private lateinit var googleMap: GoogleMap

    private val markersThreshold by lazy { resources.getDimension(R.dimen.city_details_collapse_threshold) }

    private val citiesMarkersAndAreas = mutableListOf<Pair<Marker, List<Polygon>>>()

    private var currentCity: City? = null

    private var currentZoom: Float = 0f

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

        showOrHideMarkers()
    }

    private fun showOrHideMarkers() {
        val zoom = googleMap.cameraPosition.zoom

        if ((currentZoom - zoom).absoluteValue > .1f) {
            currentZoom = zoom

            citiesMarkersAndAreas.forEach { (marker, polis) ->
                val p1 = googleMap.projection.toScreenLocation(marker.city.workingBounds.northeast)
                val p2 = googleMap.projection.toScreenLocation(marker.city.workingBounds.southwest)
                val distance = p1.distanceTo(p2)

                marker.isVisible = distance < markersThreshold
                polis.forEach { it.isVisible = !marker.isVisible }
            }
        }
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        showCity(marker.city, true)
        return true
    }

    private fun clearCities() {
        citiesMarkersAndAreas.forEach { (marker, polis) ->
            marker.remove()
            polis.forEach(Polygon::remove)
        }
    }

    override fun showCities(workingAreas: List<Triple<City, MarkerOptions, List<PolygonOptions>>>) {
        clearCities()

        workingAreas.forEach { (city, markerOpt, poliOpts) ->
            val marker = googleMap.addMarker(markerOpt).apply { tag = city }
            val polygons = poliOpts.map(googleMap::addPolygon)

            citiesMarkersAndAreas.add(marker to polygons)
        }

        onCameraMove()
    }

    override fun showCity(city: City?, focusInWholeWorkingArea: Boolean) {
        if (city != currentCity) {
            currentCity = city

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

    private fun Point.distanceTo(other: Point) =
        Math.sqrt(Math.pow(x.toDouble() - other.x, 2.0) + Math.pow(y.toDouble() - other.y, 2.0))


    companion object {

        fun makeIntent(context: Context, initialData: InitialData?) =
            Intent(context, ExploreActivity::class.java).apply {
                putExtra(EXTRA_INITIAL_DATA, initialData)
            }

    }

}
