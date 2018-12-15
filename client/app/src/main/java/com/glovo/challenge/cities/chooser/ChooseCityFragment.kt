package com.glovo.challenge.cities.chooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glovo.challenge.R
import com.glovo.challenge.cities.ExploreActivity
import com.glovo.challenge.data.models.City
import dagger.android.support.DaggerAppCompatDialogFragment
import kotlinx.android.synthetic.main.fragment_city_chooser.*
import javax.inject.Inject

class ChooseCityFragment : DaggerAppCompatDialogFragment(), ChooseCityContract.View {

    @Inject
    internal lateinit var presenter: ChooseCityContract.Presenter

    init {
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_city_chooser, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewSwitcher.displayedChild = 1
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart()
    }

    override fun showItems(items: List<ChooseCityContract.Item>) {
        viewSwitcher.displayedChild = 0
        recyclerView.adapter = ChooseCityAdapter(items, ::onCitySelected)
    }

    private fun onCitySelected(city: City) {
        val intent = ExploreActivity.makeIntent(context!!, city)

        startActivity(intent)
        dismiss()
    }

    override fun onStop() {
        super.onStop()

        presenter.onStop()
    }

}
