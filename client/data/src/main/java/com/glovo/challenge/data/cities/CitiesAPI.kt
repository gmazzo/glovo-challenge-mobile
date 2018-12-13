package com.glovo.challenge.data.cities

import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface CitiesAPI {

    @GET("cities")
    fun list(): Single<List<CityDTO>>

    @GET("cities/{cityCode}")
    fun getDetails(@Path("cityCode") cityCode: String): Maybe<CityDTO>

}
