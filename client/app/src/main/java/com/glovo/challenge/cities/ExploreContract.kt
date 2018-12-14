package com.glovo.challenge.cities

import com.glovo.challenge.data.models.City
import com.google.android.gms.maps.model.PolygonOptions

internal interface ExploreContract {

    interface View {

        fun showWorkAreas(workingAreas: List<Pair<City, PolygonOptions>>)

        fun showCity(city: City)

    }

    interface Presenter {

        fun onMapReady()

        fun onStop()

    }

}
