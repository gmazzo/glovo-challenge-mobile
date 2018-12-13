package com.glovo.challenge.data.countries

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [CountriesModule.Companion::class])
internal interface CountriesModule {

    @Binds
    fun bindRepository(impl: CountriesRepositoryImpl): CountriesRepository

    @Module
    companion object {

        @Provides
        fun provideAPI(retrofit: Retrofit): CountriesAPI =
            retrofit.create(CountriesAPI::class.java)

    }

}
