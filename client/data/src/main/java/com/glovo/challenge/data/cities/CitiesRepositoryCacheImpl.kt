package com.glovo.challenge.data.cities

import com.glovo.challenge.data.models.City
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Named

internal class CitiesRepositoryCacheImpl @Inject constructor(
    @Named("base") private val base: CitiesRepository
) : CitiesRepository {

    private val cachedListCities = base.listCities().cache()!!

    private val getCityDetails = mutableMapOf<String, Maybe<City>>()

    override fun listCities() = cachedListCities

    override fun getCityDetails(cityCode: String) =
        getCityDetails.getOrPut(cityCode) {
            base.getCityDetails(cityCode).cache()
        }

}
