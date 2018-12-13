package com.glovo.challenge

import com.glovo.challenge.data.DataComponent
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class],
    dependencies = [DataComponent::class]
)
interface ApplicationInjector : AndroidInjector<Application> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<Application>() {

        abstract fun dataComponent(dataComponent: DataComponent): Builder

    }

}

