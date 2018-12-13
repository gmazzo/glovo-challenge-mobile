package com.glovo.challenge

import com.glovo.challenge.data.DaggerDataComponent
import dagger.android.support.DaggerApplication

class Application : DaggerApplication() {

    override fun applicationInjector() = DaggerApplicationInjector.builder()
        .dataComponent(
            DaggerDataComponent.builder()
                .build()
        )
        .build()!!

}
