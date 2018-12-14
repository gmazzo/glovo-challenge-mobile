package com.glovo.challenge.data

import android.app.Application
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.countries.CountriesRepository
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class])
interface DataComponent {

    fun countriesRepository(): CountriesRepository

    fun citiesRepository(): CitiesRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun appplication(application: Application): Builder

        fun build(): DataComponent

    }

}
