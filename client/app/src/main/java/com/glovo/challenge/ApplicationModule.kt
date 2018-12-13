package com.glovo.challenge

import com.glovo.challenge.cities.details.CityDetailsFragment
import com.glovo.challenge.cities.details.CityDetailsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ApplicationModule {

    @ContributesAndroidInjector(modules = [CityDetailsModule::class])
    fun cityDetailsFragment(): CityDetailsFragment

}
