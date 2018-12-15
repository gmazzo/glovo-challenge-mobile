package com.glovo.challenge.cities.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glovo.challenge.R
import com.glovo.challenge.cities.chooser.ChooseCityFragment
import kotlinx.android.synthetic.main.fragment_city_no_details.*

class CityNoDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_city_no_details, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            fragmentManager!!.beginTransaction()
                .add(ChooseCityFragment(), null)
                .commit()
        }
    }

}
