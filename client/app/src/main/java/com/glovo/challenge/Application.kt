package com.glovo.challenge

import android.util.Log
import android.widget.Toast
import com.glovo.challenge.data.DaggerDataComponent
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins

class Application : DaggerApplication() {

    override fun applicationInjector() = DaggerApplicationInjector.builder()
        .dataComponent(
            DaggerDataComponent.builder()
                .endpoint(BuildConfig.API_ENDPOINT)
                .application(this)
                .build()
        )
        .create(this)!!

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler {
            Log.e("Rx", it.localizedMessage, it)

            if (BuildConfig.DEBUG) {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()

            } else {
                // let it crash and generate a report
                throw it;
            }
        }
    }

}
