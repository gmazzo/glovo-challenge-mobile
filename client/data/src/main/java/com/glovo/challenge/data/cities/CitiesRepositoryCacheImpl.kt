package com.glovo.challenge.data.cities

import com.glovo.challenge.data.models.City
import io.reactivex.Maybe
import javax.inject.Inject

internal class CitiesRepositoryCacheImpl @Inject constructor(
    private val baseImpl: CitiesRepositoryImpl
) : CitiesRepository {

    private val cachedListCities = baseImpl.listCities().cache()!!

    private val getCityDetails = mutableMapOf<String, Maybe<City>>()

    override fun listCities() = cachedListCities

    override fun getCityDetails(cityCode: String) =
        getCityDetails.getOrPut(cityCode) {
            baseImpl.getCityDetails(cityCode).cache()
        }

}
