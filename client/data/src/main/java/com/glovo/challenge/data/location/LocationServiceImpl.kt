package com.glovo.challenge.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.glovo.utils.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Tasks
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class LocationServiceImpl @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationService {

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation() = Maybe.fromCallable<Location> {
        if (context.hasLocationPermission)
            Tasks.await(client.lastLocation, 3, TimeUnit.SECONDS)
        else
            null

    }.subscribeOn(Schedulers.io())!!

}
