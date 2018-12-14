package com.glovo.challenge.splash

import android.location.Location
import com.glovo.challenge.data.models.City

internal interface SplashContract {

    interface View {

        fun onReady(cities: List<City>, location: Location?)

    }

    interface Presenter {

        fun onStart()

        fun onStop()

    }

}
