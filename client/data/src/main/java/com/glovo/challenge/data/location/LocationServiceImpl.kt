package com.glovo.challenge.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.Maybe
import javax.inject.Inject

internal class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationService {

    override fun getCurrentLocation() = Maybe.fromCallable<Location> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            client.lastLocation.result
        } else {
            null
        }
    }!!

}
