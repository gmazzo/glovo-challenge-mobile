package com.glovo.challenge.cities

import com.glovo.challenge.data.models.City
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

internal interface ExploreContract {

    interface View {

        fun showCities(workingAreas: List<Triple<City, MarkerOptions, List<PolygonOptions>>>)

        fun showCity(city: City?, focusInWholeWorkingArea: Boolean)

    }

    interface Presenter {

        fun onMapReady()

        fun onMapFocusTarget(target: LatLng)

        fun onStop()

    }

}
