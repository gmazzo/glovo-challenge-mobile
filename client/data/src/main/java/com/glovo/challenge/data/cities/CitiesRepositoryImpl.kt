package com.glovo.challenge.data.cities

import com.glovo.challenge.data.countries.CountriesRepository
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
import io.reactivex.functions.BiFunction
import javax.inject.Inject

internal class CitiesRepositoryImpl @Inject constructor(
    private val api: CitiesAPI,
    countriesRepository: CountriesRepository
) : CitiesRepository {

    private val countriesByCode = countriesRepository.list()
        .map {
            it.map { country ->
                country.code to country
            }.toMap()
        }
        .cache()

    override fun list() = api.list()
        .zipWith(countriesByCode, BiFunction(::buildModels))!!

    private fun buildModels(dtos: List<CityDTO>, countries: Map<String, Country>) =
        dtos.map { dto -> buildModel(dto, countries) }

    private fun buildModel(dto: CityDTO, countries: Map<String, Country>) = City(
        code = dto.code,
        name = dto.name,
        country = countries[dto.countryCode]!!,
        workingArea = dto.workingArea
    )

}
