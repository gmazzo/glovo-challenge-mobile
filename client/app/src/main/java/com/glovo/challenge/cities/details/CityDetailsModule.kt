package com.glovo.challenge.cities.details

import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal abstract class CityDetailsModule {

    @Binds
    abstract fun bindView(impl: CityDetailsFragment): CityDetailsContract.View

    @Binds
    abstract fun bindPresenter(impl: CityDetailsPresenter): CityDetailsContract.Presenter

    @Module
    companion object {

        const val EXTRA_CITY_CODE = "cityCode"

        @Provides
        @Named(EXTRA_CITY_CODE)
        @JvmStatic
        fun provideCities(fragment: CityDetailsFragment) =
            fragment.arguments!!.getString(EXTRA_CITY_CODE)!!

    }

}