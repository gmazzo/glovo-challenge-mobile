package com.glovo.challenge.cities

import com.glovo.challenge.data.models.City
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

internal interface ExploreContract {

    interface View {

        fun showCities(citiesGeoData: List<Triple<City, MarkerOptions, List<PolygonOptions>>>)

        fun showCity(city: City?, focusInWholeWorkingArea: Boolean)

        fun onLoadReady()

    }

    interface Presenter {

        fun setFocusOnInitialLocation(focusOnInitialLocation: Boolean)

        fun onMapReady()

        fun onMapFocusTarget(target: LatLng, ignoreIfNotFound: Boolean)

        fun onStop()

    }

}
