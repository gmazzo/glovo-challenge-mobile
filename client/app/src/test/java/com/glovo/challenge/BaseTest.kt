package com.glovo.challenge

import io.reactivex.internal.schedulers.ImmediateThinScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

// TODO move this to a shared artifact
@RunWith(RobolectricTestRunner::class)
abstract class BaseTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()!!

    companion object {

        @JvmStatic
        @BeforeClass
        fun setupRx() {
            val scheduler = ImmediateThinScheduler.INSTANCE
            RxJavaPlugins.setSingleSchedulerHandler { scheduler }
            RxJavaPlugins.setIoSchedulerHandler { scheduler }
            RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        }

    }

}

