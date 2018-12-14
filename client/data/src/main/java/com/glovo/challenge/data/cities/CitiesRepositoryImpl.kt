package com.glovo.challenge.data.cities

import com.glovo.challenge.data.countries.CountriesRepository
import com.glovo.challenge.data.geo.GeoService
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
import io.reactivex.functions.BiFunction
import javax.inject.Inject

internal class CitiesRepositoryImpl @Inject constructor(
    private val api: CitiesAPI,
    countriesRepository: CountriesRepository,
    private val geoService: GeoService
) : CitiesRepository {

    private val countriesByCode = countriesRepository
        .listCountries()
        .map {
            it.map { country ->
                country.code to country
            }.toMap()
        }
        .cache()

    override fun listCities() = api
        .list()
        .zipWith(countriesByCode, BiFunction(::buildModels))!!

    override fun getCityDetails(cityCode: String) = api
        .getDetails(cityCode)
        .zipWith(countriesByCode.toMaybe(), BiFunction(::buildModel))!!

    private fun buildModels(dtos: List<CityDTO>, countries: Map<String, Country>) =
        dtos.map { dto -> buildModel(dto, countries) }

    private fun buildModel(dto: CityDTO, countries: Map<String, Country>) = City(
        code = dto.code,
        name = dto.name,
        country = countries[dto.countryCode]!!,
        workingArea = geoService.decodePolygons(dto.workingArea)
    )

}
