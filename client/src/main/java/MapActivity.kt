package com.glovo.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, SupportMapFragment())
                .commitNow()
        }
    }

}
