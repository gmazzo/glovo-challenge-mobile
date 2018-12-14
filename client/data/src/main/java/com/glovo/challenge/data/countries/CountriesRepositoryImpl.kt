package com.glovo.challenge.data.countries

import com.glovo.challenge.data.models.Country
import javax.inject.Inject

internal class CountriesRepositoryImpl @Inject constructor(
    private val api: CountriesAPI
) : CountriesRepository {

    override fun listCountries() = api
        .list()
        .map { it.map(::buildModel) }!!

    private fun buildModel(dto: CountryDTO) = Country(
        code = dto.code,
        name = dto.name
    )

}
