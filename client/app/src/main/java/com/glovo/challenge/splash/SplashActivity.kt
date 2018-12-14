package com.glovo.challenge.splash

import android.os.Bundle
import com.glovo.challenge.R
import com.glovo.challenge.cities.ExploreActivity
import com.glovo.challenge.models.InitialData
import dagger.android.DaggerActivity
import javax.inject.Inject

class SplashActivity : DaggerActivity(), SplashContract.View {

    @Inject
    internal lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        // TODO ask for permissions
        
        presenter.onStart()
    }

    override fun onReady(initialData: InitialData) {
        val intent = ExploreActivity.makeIntent(this, initialData)

        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

}
