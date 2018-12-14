package com.glovo.challenge.splash

import dagger.Binds
import dagger.Module

@Module
internal interface SplashModule {

    @Binds
    fun bindView(impl: SplashActivity): SplashContract.View

    @Binds
    fun bindPresenter(impl: SplashPresenter): SplashContract.Presenter

}
