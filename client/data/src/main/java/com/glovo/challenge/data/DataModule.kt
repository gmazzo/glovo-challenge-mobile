package com.glovo.challenge.data

import android.app.Application
import android.content.Context
import com.glovo.challenge.data.cities.CitiesModule
import com.glovo.challenge.data.countries.CountriesModule
import com.glovo.challenge.data.geo.GeoModule
import com.glovo.challenge.data.location.LocationModule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module(
    includes = [
        DataModule.Bindings::class,
        CountriesModule::class,
        CitiesModule::class,
        LocationModule::class,
        GeoModule::class]
)
internal class DataModule {

    @Provides
    @Reusable
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Reusable
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
        .build()

    @Provides
    @Reusable
    fun provideRetrofit(@Endpoint endpoint: String, client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .baseUrl(endpoint)
        .build()

    @Module
    interface Bindings {

        @Binds
        fun bindContext(impl: Application): Context

    }

}
