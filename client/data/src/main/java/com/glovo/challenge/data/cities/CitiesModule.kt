package com.glovo.challenge.data.cities

import com.glovo.challenge.data.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Provider

@Module
internal abstract class CitiesModule {

    @Binds
    @Named("base")
    abstract fun bindRepository(impl: CitiesRepositoryImpl): CitiesRepository

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideCitiesRepository(
            @Named("base") base: Provider<CitiesRepository>,
            cache: Provider<CitiesRepositoryCacheImpl>
        ): CitiesRepository =
            (if (BuildConfig.USE_CACHE) cache else base).get()

        @Provides
        @Reusable
        @JvmStatic
        fun provideAPI(retrofit: Retrofit): CitiesAPI =
            retrofit.create(CitiesAPI::class.java)

    }

}
