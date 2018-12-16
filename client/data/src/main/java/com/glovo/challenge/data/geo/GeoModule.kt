package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.locationtech.jts.geom.GeometryFactory
import javax.inject.Provider

@Module
internal abstract class GeoModule {

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideGeoService(base: Provider<GeoServiceImpl>, merger: Provider<GeoServiceMergerImpl>): GeoService =
            (if (BuildConfig.MERGE_POLYGONS) merger else base).get()

        @Provides
        @Reusable
        @JvmStatic
        fun provideGeometryFactory() = GeometryFactory()

    }

}
