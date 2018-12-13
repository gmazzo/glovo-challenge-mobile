package com.glovo.challenge.data.countries

import io.reactivex.Single
import retrofit2.http.GET

internal interface CountriesAPI {

    @GET("countries")
    fun list(): Single<List<CountryDTO>>

}
