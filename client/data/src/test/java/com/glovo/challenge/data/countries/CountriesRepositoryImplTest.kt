package com.glovo.challenge.data.countries

import com.glovo.challenge.data.BaseTest
import com.glovo.challenge.data.models.Country
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

class CountriesRepositoryImplTest : BaseTest() {

    @Mock
    private lateinit var api: CountriesAPI

    private val impl by lazy { CountriesRepositoryImpl(api) }

    @Test
    fun testList() {
        val dtos = listOf(CountryDTO("A", "aa"), CountryDTO("B", "bb"))
        val expected = listOf(Country("A", "aa"), Country("B", "bb"))

        `when`(api.list()).thenReturn(Single.just(dtos))

        val result = impl.list().blockingGet()

        assertEquals(expected, result)
    }

}