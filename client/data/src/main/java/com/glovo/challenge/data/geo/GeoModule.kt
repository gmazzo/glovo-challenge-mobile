package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.locationtech.jts.geom.GeometryFactory
import javax.inject.Provider

@Module
internal class GeoModule {

    @Provides
    @Reusable
    fun provideGeoService(
        base: Provider<GeoServiceImpl>, merger: Provider<GeoServiceMergerImpl>
    ): GeoService =
        (if (BuildConfig.MERGE_POLYGONS) merger else base).get()

    @Provides
    @Reusable
    fun provideGeometryFactory() = GeometryFactory()

}
