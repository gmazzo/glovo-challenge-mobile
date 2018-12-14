package com.glovo.challenge.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.PermissionChecker
import com.glovo.challenge.data.BaseTest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class LocationServiceImplTest(
    @PermissionChecker.PermissionResult private val permission: Int,
    private val currentLocation: Location?,
    private val expectedLocation: Location?
) : BaseTest() {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var client: FusedLocationProviderClient

    @Mock
    lateinit var task: Task<Location>

    private val impl by lazy { LocationServiceImpl(context, client) }

    @Test
    fun testGetCurrentLocation() {
        `when`(context.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt()))
            .thenReturn(permission)
        `when`(client.lastLocation).thenReturn(task)
        `when`(task.result).thenReturn(currentLocation)

        val result = impl.getCurrentLocation().blockingGet()

        assertEquals(expectedLocation, result)
    }

    companion object {
        private val TEST_LOCATION = Location("aLocation")

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data() = listOf(
            arrayOf(PackageManager.PERMISSION_DENIED, null, null),
            arrayOf(PackageManager.PERMISSION_DENIED, TEST_LOCATION, null),
            arrayOf(PackageManager.PERMISSION_GRANTED, null, null),
            arrayOf(PackageManager.PERMISSION_GRANTED, TEST_LOCATION, TEST_LOCATION)
        )
    }

}
