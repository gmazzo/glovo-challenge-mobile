package com.glovo.challenge.cities.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.glovo.challenge.R
import com.glovo.challenge.cities.details.CityDetailsModule.Companion.EXTRA_CITY_CODE
import com.glovo.challenge.data.models.City
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_city_details.*
import javax.inject.Inject

class CityDetailsFragment : DaggerFragment(), CityDetailsContract.View {

    @Inject
    internal lateinit var presenter: CityDetailsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_city_details, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewSwitcher.displayedChild = 1
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

    override fun showDetails(city: City) {
        viewSwitcher.displayedChild = 0
        country.text = city.country.name
        name.text = city.name
        currency.text = city.currency
        timezone.text = city.timeZone
    }

    companion object {

        fun newInstance(cityCode: String) = CityDetailsFragment().apply {
            arguments = bundleOf(EXTRA_CITY_CODE to cityCode)
        }

    }

}
