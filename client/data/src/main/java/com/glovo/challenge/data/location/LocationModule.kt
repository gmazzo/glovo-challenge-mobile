package com.glovo.challenge.data.location

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
internal abstract class LocationModule {

    @Binds
    @Reusable
    abstract fun bindLocationService(impl: LocationServiceImpl): LocationService

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideFuseClient(context: Context) =
            LocationServices.getFusedLocationProviderClient(context)!!

    }


}
