package com.glovo.challenge.cities.chooser

import dagger.Binds
import dagger.Module

@Module
internal interface ChooseCityModule {

    @Binds
    fun bindView(impl: ChooseCityFragment): ChooseCityContract.View

    @Binds
    fun bindPresenter(impl: ChooseCityPresenter): ChooseCityContract.Presenter

}
