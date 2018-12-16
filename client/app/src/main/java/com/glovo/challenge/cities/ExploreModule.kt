package com.glovo.challenge.cities

import android.content.Context
import androidx.core.content.ContextCompat
import com.glovo.challenge.R
import com.glovo.challenge.models.InitialData
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal abstract class ExploreModule {

    @Binds
    abstract fun bindView(impl: ExploreActivity): ExploreContract.View

    @Binds
    abstract fun bindPresenter(impl: ExplorePresenter): ExploreContract.Presenter

    @Module
    companion object {
        const val EXTRA_INITIAL_DATA = "initialData"

        @Provides
        @JvmStatic
        @Named("workingAreaFillColor")
        fun provideWorkingAreaFillColor(context: Context) =
            ContextCompat.getColor(context, R.color.working_areas_fill)

        @Provides
        @JvmStatic
        @Named("workingAreaBorderColor")
        fun provideWorkingAreaBorderColor(context: Context) =
            ContextCompat.getColor(context, R.color.working_areas_border)

        @Provides
        @JvmStatic
        fun provideMarkerIcon() =
            BitmapDescriptorFactory.defaultMarker()!!

        @Provides
        @JvmStatic
        fun provideCities(activity: ExploreActivity): InitialData? =
            activity.intent.getParcelableExtra(EXTRA_INITIAL_DATA)

    }

}
