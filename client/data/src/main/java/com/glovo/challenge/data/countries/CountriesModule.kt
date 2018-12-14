package com.glovo.challenge.data.countries

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal abstract class CountriesModule {

    @Binds
    abstract fun bindRepository(impl: CountriesRepositoryImpl): CountriesRepository

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideAPI(retrofit: Retrofit): CountriesAPI =
            retrofit.create(CountriesAPI::class.java)

    }

}
