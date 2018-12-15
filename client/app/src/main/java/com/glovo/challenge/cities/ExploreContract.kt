package com.glovo.challenge.cities

import com.glovo.challenge.data.models.City
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

internal interface ExploreContract {

    interface View {

        fun onLoadReady()

        fun showCities(citiesGeoData: List<Triple<City, MarkerOptions, List<PolygonOptions>>>)

        fun showCity(city: City?, focusInWholeWorkingArea: Boolean)

        fun showChooseCities()

    }

    interface Presenter {

        fun setFocusOnInitialLocation(focusOnInitialLocation: Boolean)

        fun onMapReady()

        fun onMapFocusTarget(target: LatLng)

        fun onPickMyLocation()

        fun onStop()

    }

}
