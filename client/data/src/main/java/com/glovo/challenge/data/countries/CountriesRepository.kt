package com.glovo.challenge.data.countries

import com.glovo.challenge.data.models.Country
import io.reactivex.Single

interface CountriesRepository {

    fun listCountries(): Single<List<Country>>

}
