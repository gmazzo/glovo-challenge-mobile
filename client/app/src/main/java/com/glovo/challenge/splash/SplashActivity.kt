package com.glovo.challenge.splash

import android.os.Bundle
import com.glovo.challenge.R
import com.glovo.challenge.cities.ExploreActivity
import com.glovo.challenge.models.InitialData
import com.glovo.utils.hasLocationPermission
import com.glovo.utils.requestLocationPermission
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

        askLocationPermission()
    }

    private fun askLocationPermission() {
        if (hasLocationPermission) {
            presenter.onStart()

        } else {
            requestLocationPermission(RC_ASK_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_ASK_PERMISSIONS) {
            // no mather the result here, we have to continue
            presenter.onStart()
        }
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

    companion object {

        const val RC_ASK_PERMISSIONS = 1000

    }

}
