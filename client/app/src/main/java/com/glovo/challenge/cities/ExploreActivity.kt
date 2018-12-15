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
import com.google.android.gms.maps.model.PolygonOptions
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ExploreActivity : DaggerAppCompatActivity(), ExploreContract.View {

    @Inject
    internal lateinit var presenter: ExploreContract.Presenter

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_explore)
    }

    override fun onStart() {
        super.onStart()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it

            @SuppressLint("MissingPermission")
            map.isMyLocationEnabled = hasLocationPermission

            map.setOnPolygonClickListener { poly ->
                showCity(poly.tag as City)
            }

            presenter.onMapReady()
        }
    }

    override fun showWorkAreas(workingAreas: List<Pair<City, PolygonOptions>>) {
        workingAreas.forEach { (city, poly) ->
            map.addPolygon(poly).apply {
                tag = city
                isClickable = true
            }
        }
    }

    override fun showCity(city: City) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                city.workingBounds,
                resources.getDimensionPixelOffset(R.dimen.working_areas_padding)
            )
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.cityDetails, CityDetailsFragment.newInstance(city.code))
            .commitNow()
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
