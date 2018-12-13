package com.glovo.challenge.cities.details

import com.glovo.challenge.data.models.City

internal interface CityDetailsContract {

    interface View {

        fun showDetails(city: City)

    }

    interface Presenter {

        fun onStart()

        fun onStop()

    }

}
