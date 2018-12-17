package com.glovo.challenge.data.geo

import com.glovo.challenge.data.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.locationtech.jts.geom.GeometryFactory
import javax.inject.Named
import javax.inject.Provider

@Module
internal abstract class GeoModule {

    @Binds
    @Named("base")
    abstract fun provideBaseGeoService(impl: GeoServiceImpl): GeoService

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideGeoService(
            @Named("base") base: Provider<GeoService>,
            merger: Provider<GeoServiceMergerImpl>
        ): GeoService =
            (if (BuildConfig.MERGE_POLYGONS) merger else base).get()

        @Provides
        @Reusable
        @JvmStatic
        fun provideGeometryFactory() = GeometryFactory()

    }

}
