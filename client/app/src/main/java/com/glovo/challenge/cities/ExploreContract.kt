package com.glovo.challenge.cities

import com.glovo.challenge.data.models.City
import com.google.android.gms.maps.model.LatLng

internal interface ExploreContract {

    interface View {

        fun onLoadReady()

        fun showCitiesGeo(citiesGeoData: List<CityGeoData>)

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
