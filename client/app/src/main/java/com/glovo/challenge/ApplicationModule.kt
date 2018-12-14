package com.glovo.challenge

import android.content.Context
import com.glovo.challenge.cities.ExploreActivity
import com.glovo.challenge.cities.ExploreModule
import com.glovo.challenge.cities.details.CityDetailsFragment
import com.glovo.challenge.cities.details.CityDetailsModule
import com.glovo.challenge.splash.SplashActivity
import com.glovo.challenge.splash.SplashModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ApplicationModule {

    @Binds
    fun bindContext(impl: Application): Context

    @ContributesAndroidInjector(modules = [SplashModule::class])
    fun splashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [ExploreModule::class])
    fun exploreActivity(): ExploreActivity

    @ContributesAndroidInjector(modules = [CityDetailsModule::class])
    fun cityDetailsFragment(): CityDetailsFragment

}
