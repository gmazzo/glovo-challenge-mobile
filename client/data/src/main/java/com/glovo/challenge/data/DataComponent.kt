package com.glovo.challenge.data

import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.countries.CountriesRepository
import dagger.Component

@Component(modules = [DataModule::class])
interface DataComponent {

    fun countriesRepository(): CountriesRepository

    fun citiesRepository(): CitiesRepository

}
