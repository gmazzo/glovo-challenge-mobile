package com.glovo.challenge

import com.glovo.challenge.cities.details.CityDetailsFragment
import com.glovo.challenge.cities.details.CityDetailsModule
import com.glovo.challenge.splash.SplashActivity
import com.glovo.challenge.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ApplicationModule {

    @ContributesAndroidInjector(modules = [SplashModule::class])
    fun splashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [CityDetailsModule::class])
    fun cityDetailsFragment(): CityDetailsFragment

}
