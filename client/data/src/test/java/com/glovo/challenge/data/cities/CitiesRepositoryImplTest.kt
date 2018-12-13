package com.glovo.challenge.data.cities

import com.glovo.challenge.data.BaseTest
import com.glovo.challenge.data.countries.CountriesRepository
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

class CitiesRepositoryImplTest : BaseTest() {

    @Mock
    private lateinit var api: CitiesAPI

    @Mock
    private lateinit var countriesRepo: CountriesRepository

    private val impl by lazy { CitiesRepositoryImpl(api, countriesRepo) }

    @Test
    fun testList() {
        val dtos = listOf(DTO1, DTO2, DTO3)
        val expected = listOf(
            City("SCL", "Santiago", COUNTRY_CL, listOf("aWorkingArea")),
            City("BUE", "Buenos Aires", COUNTRY_AR, listOf("aWorkingArea")),
            City("BCN", "Barcelona", COUNTRY_ES, emptyList())
        )

        `when`(api.list()).thenReturn(Single.just(dtos))
        `when`(countriesRepo.list()).thenReturn(Single.just(listOf(COUNTRY_CL, COUNTRY_AR, COUNTRY_ES)))

        val result = impl.list().blockingGet()

        assertEquals(expected, result)
    }

    companion object {

        private val COUNTRY_CL = Country("CL", "Chile")
        private val COUNTRY_AR = Country("AR", "Argentina")
        private val COUNTRY_ES = Country("ES", "España")

        private val DTO1 = CityDTO(
            code = "SCL",
            name = "Santiago",
            currency = null,
            countryCode = "CL",
            enabled = null,
            timeZone = null,
            workingArea = listOf("aWorkingArea"),
            busy = null,
            languageCode = null
        )

        private val DTO2 = DTO1.copy("BUE", "Buenos Aires", countryCode = "AR")

        private val DTO3 = DTO1.copy("BCN", "Barcelona", countryCode = "ES", workingArea = emptyList())

    }

}