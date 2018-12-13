package com.glovo.challenge.data.cities

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [CitiesModule.Companion::class])
internal interface CitiesModule {

    @Binds
    fun bindRepository(impl: CitiesRepositoryImpl): CitiesRepository

    @Module
    companion object {

        @Provides
        fun provideAPI(retrofit: Retrofit): CitiesAPI =
            retrofit.create(CitiesAPI::class.java)

    }

}
