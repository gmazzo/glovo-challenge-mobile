package com.glovo.challenge.data.geo

import dagger.Binds
import dagger.Module

@Module
internal interface GeoModule {

    @Binds
    abstract fun bindGeoService(impl: GeoServiceImpl): GeoService

}
