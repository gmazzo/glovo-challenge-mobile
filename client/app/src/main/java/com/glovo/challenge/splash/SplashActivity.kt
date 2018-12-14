package com.glovo.challenge.splash

import android.location.Location
import android.os.Bundle
import com.glovo.challenge.MainActivity
import com.glovo.challenge.R
import com.glovo.challenge.data.models.City
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

        presenter.onStart()
    }

    override fun onReady(cities: List<City>, location: Location?) {
        val intent = MainActivity.makeIntent(this, cities.toTypedArray(), location)

        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

}
