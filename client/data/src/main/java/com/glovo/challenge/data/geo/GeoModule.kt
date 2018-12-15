package com.glovo.challenge.data.geo

import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface GeoModule {

    @Binds
    @Reusable
    fun bindGeoService(impl: GeoServiceImpl): GeoService

}
