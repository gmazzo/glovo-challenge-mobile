package com.glovo.challenge.cities.details

import com.glovo.challenge.data.models.City
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

        const val EXTRA_CITY = "city"

        @Provides
        @Named(EXTRA_CITY)
        @JvmStatic
        fun provideCities(fragment: CityDetailsFragment): City =
            fragment.arguments!!.getParcelable(EXTRA_CITY)!!

    }

}