package com.glovo.challenge.data.geo

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import math.geom2d.polygon.convhull.ConvexHull2D
import math.geom2d.polygon.convhull.JarvisMarch2D

@Module
internal abstract class GeoModule {

    @Binds
    @Reusable
    abstract fun bindGeoService(impl: GeoServiceImpl): GeoService

    @Module
    companion object {

        @Provides
        @Reusable
        @JvmStatic
        fun provideConvexHull2D(): ConvexHull2D = JarvisMarch2D()

    }

}
