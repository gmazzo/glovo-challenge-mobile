package com.glovo.challenge.data.cities

import com.glovo.challenge.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import javax.inject.Provider

@Module
internal class CitiesModule {

    @Provides
    @Reusable
    fun provideCitiesRepository(
        base: Provider<CitiesRepositoryImpl>,
        cache: Provider<CitiesRepositoryCacheImpl>
    ): CitiesRepository =
        (if (BuildConfig.USE_CACHE) cache else base).get()

    @Provides
    @Reusable
    fun provideAPI(retrofit: Retrofit): CitiesAPI =
        retrofit.create(CitiesAPI::class.java)

}
