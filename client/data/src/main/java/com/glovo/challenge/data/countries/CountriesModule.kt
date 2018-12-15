package com.glovo.challenge.data.countries

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit

@Module
internal abstract class CountriesModule {

    @Binds
    @Reusable
    abstract fun bindRepository(impl: CountriesRepositoryImpl): CountriesRepository

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideAPI(retrofit: Retrofit): CountriesAPI =
            retrofit.create(CountriesAPI::class.java)

    }

}
