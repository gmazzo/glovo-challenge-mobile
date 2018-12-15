package com.glovo.challenge.cities.chooser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glovo.challenge.R
import com.glovo.challenge.data.models.City
import kotlinx.android.synthetic.main.fragment_city_chooser_item_city.view.*
import kotlinx.android.synthetic.main.fragment_city_chooser_item_country.view.*

internal class ChooseCityAdapter(
    private val items: List<ChooseCityContract.Item>,
    private val onCitySelectedListener: (City) -> Unit
) : RecyclerView.Adapter<ChooseCityViewHolder>() {

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
        if (items[position].country != null) R.layout.fragment_city_chooser_item_country
        else R.layout.fragment_city_chooser_item_city

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater
        .from(parent.context)
        .inflate(viewType /* layoutRes */, parent, false)
        .let(::ChooseCityViewHolder)

    override fun onBindViewHolder(holder: ChooseCityViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            when {
                // binds a Country
                item.country != null -> country.text = item.country.name

                // binds a City
                item.city != null -> {
                    city.text = item.city.name

                    setOnClickListener { onCitySelectedListener(item.city) }
                }
            }
        }
    }

}
