package com.glovo.challenge.data.location

import android.location.Location
import io.reactivex.Maybe

interface LocationService {

    fun getCurrentLocation(): Maybe<Location>

}
