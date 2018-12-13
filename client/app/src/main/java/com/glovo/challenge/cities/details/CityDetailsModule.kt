package com.glovo.challenge.cities.details

import dagger.Binds
import dagger.Module

@Module
internal interface CityDetailsModule {

    @Binds
    fun bindView(impl: CityDetailsFragment): CityDetailsContract.View

    @Binds
    fun bindPresenter(impl: CityDetailsPresenter): CityDetailsContract.Presenter

}