package com.glovo.challenge.cities.chooser

import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country

interface ChooseCityContract {

    interface View {

        fun showItems(items: List<Item>)

    }

    interface Presenter {

        fun onStart()

        fun onStop()

    }

    data class Item(
        val country: Country? = null,
        val city: City? = null
    )

}
