package com.glovo.challenge.data.cities

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit

@Module
internal abstract class CitiesModule {

    @Binds
    @Reusable
    abstract fun bindRepository(impl: CitiesRepositoryImpl): CitiesRepository

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideAPI(retrofit: Retrofit): CitiesAPI =
            retrofit.create(CitiesAPI::class.java)

    }

}
