package com.glovo.challenge.splash

import com.glovo.challenge.models.InitialData

internal interface SplashContract {

    interface View {

        fun onReady(initialData: InitialData)

    }

    interface Presenter {

        fun onStart()

        fun onStop()

    }

}
