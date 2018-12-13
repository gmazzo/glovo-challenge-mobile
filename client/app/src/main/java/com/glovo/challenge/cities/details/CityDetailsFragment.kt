package com.glovo.challenge.cities.details

import com.glovo.challenge.data.models.City
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CityDetailsFragment : DaggerFragment(), CityDetailsContract.View {

    @Inject
    internal lateinit var presenter: CityDetailsPresenter

    override fun onStart() {
        super.onStart()

        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

    override fun showDetails(city: City) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
