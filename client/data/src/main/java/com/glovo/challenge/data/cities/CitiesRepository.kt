package com.glovo.challenge.data.cities

import com.glovo.challenge.data.models.City
import io.reactivex.Maybe
import io.reactivex.Single

interface CitiesRepository {

    fun listCities(): Single<List<City>>

    fun getCityDetails(cityCode: String): Maybe<City>

}
