package com.glovo.challenge.data.cities

import com.glovo.challenge.data.models.City
import io.reactivex.Single

interface CitiesRepository {

    fun list(): Single<List<City>>

}
