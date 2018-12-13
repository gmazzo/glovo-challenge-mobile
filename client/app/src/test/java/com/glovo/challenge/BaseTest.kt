package com.glovo.challenge

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()!!

}
