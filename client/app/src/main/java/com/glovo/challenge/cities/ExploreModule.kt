package com.glovo.challenge.cities

import com.glovo.challenge.models.InitialData
import dagger.Binds
import dagger.Module
import dagger.Provides

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
        fun provideCities(activity: ExploreActivity): InitialData? =
            activity.intent.getParcelableExtra(EXTRA_INITIAL_DATA)

    }

}
