package com.glovo.challenge.data.cities

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal abstract class CitiesModule {

    @Binds
    abstract fun bindRepository(impl: CitiesRepositoryImpl): CitiesRepository

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideAPI(retrofit: Retrofit): CitiesAPI =
            retrofit.create(CitiesAPI::class.java)

    }

}
