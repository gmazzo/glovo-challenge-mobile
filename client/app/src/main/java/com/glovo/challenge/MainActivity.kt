package com.glovo.challenge

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glovo.challenge.data.models.City

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    companion object {

        private const val EXTRA_CITIES = "cities"
        private const val EXTRA_LOCATION = "location"

        fun makeIntent(context: Context, cities: Array<City>?, location: Location?) =
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_CITIES, cities)
                putExtra(EXTRA_LOCATION, location)
            }

    }

}
