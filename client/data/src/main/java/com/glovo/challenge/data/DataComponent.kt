package com.glovo.challenge.data

import android.app.Application
import com.glovo.challenge.data.cities.CitiesRepository
import com.glovo.challenge.data.location.LocationService
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class])
interface DataComponent {

    fun citiesRepository(): CitiesRepository

    fun locationService(): LocationService

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun endpoint(@Endpoint endpoint: String): Builder

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): DataComponent

    }

}
