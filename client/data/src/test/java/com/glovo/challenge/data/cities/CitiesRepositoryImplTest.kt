package com.glovo.challenge.data.cities

import com.glovo.challenge.data.BaseTest
import com.glovo.challenge.data.countries.CountriesRepository
import com.glovo.challenge.data.geo.GeoService
import com.glovo.challenge.data.models.City
import com.glovo.challenge.data.models.Country
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

class CitiesRepositoryImplTest : BaseTest() {

    @Mock
    private lateinit var api: CitiesAPI

    @Mock
    private lateinit var countriesRepo: CountriesRepository

    @Mock
    private lateinit var geoService: GeoService

    private val impl by lazy { CitiesRepositoryImpl(api, countriesRepo, geoService) }

    @Before
    fun setup() {
        `when`(countriesRepo.listCountries()).thenReturn(Single.just(listOf(COUNTRY_CL, COUNTRY_AR, COUNTRY_ES)))

        `when`(geoService.decodePolygons(DTO_SCL.workingArea)).thenReturn(CITY_SCL.workingArea)
        `when`(geoService.decodePolygons(DTO_BUE.workingArea)).thenReturn(CITY_BUE.workingArea)
        `when`(geoService.decodePolygons(DTO_BCN.workingArea)).thenReturn(CITY_BCN.workingArea)
    }

    @Test
    fun testList() {
        val dtos = listOf(DTO_SCL, DTO_BUE, DTO_BCN)
        val expected = listOf(CITY_SCL, CITY_BUE, CITY_BCN)

        `when`(api.list()).thenReturn(Single.just(dtos))

        val result = impl.listCities().blockingGet()

        assertEquals(expected, result)
    }

    @Test
    fun testGetDetails_BCN() {
        testGetDetails(DTO_BCN.code, DTO_BCN, CITY_BCN)
    }

    @Test
    fun testGetDetails_BUE() {
        testGetDetails(DTO_BUE.code, DTO_BUE, CITY_BUE)
    }

    @Test
    fun testGetDetails_WithBlanksWorkingAreas() {
        testGetDetails(
            DTO_BCN.code,
            DTO_BCN.copy(
                workingArea = DTO_BCN.workingArea.toMutableList().apply {
                    add(0, "")
                    add(2, "   ")
                    add(size, "")
                }
            ),
            CITY_BCN // blanks should be stripped out
        )
    }

    @Test
    fun testGetDetails_Unknown() {
        testGetDetails("XX", null, null)
    }

    private fun testGetDetails(cityCode: String, dto: CityDTO?, expectedCity: City?) {
        `when`(api.getDetails(cityCode)).thenReturn(Maybe.fromCallable { dto })

        val result = impl.getCityDetails(cityCode).blockingGet()

        assertEquals(expectedCity, result)
    }

    companion object {

        private val DTO_SCL = CityDTO(
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

        private val DTO_BUE = DTO_SCL.copy(
            code = "BUE",
            name = "Buenos Aires",
            countryCode = "AR",
            workingArea = listOf("wa1")
        )

        private val DTO_BCN = DTO_SCL.copy(
            code = "BCN",
            name = "Barcelona",
            countryCode = "ES",
            workingArea = listOf("myWa", "anotherWA")
        )

        private val COUNTRY_CL = Country(code = "CL", name = "Chile")
        private val COUNTRY_AR = Country(code = "AR", name = "Argentina")
        private val COUNTRY_ES = Country(code = "ES", name = "España")

        private val CITY_SCL =
            City(
                code = DTO_SCL.code,
                name = DTO_SCL.name,
                country = COUNTRY_CL,
                workingArea = listOf(listOf(LatLng(1.0, -1.0)))
            )

        private val CITY_BUE =
            City(
                code = DTO_BUE.code,
                name = DTO_BUE.name,
                country = COUNTRY_AR,
                workingArea = listOf(listOf(LatLng(2.0, -2.0), LatLng(4.0, -4.0)))
            )

        private val CITY_BCN =
            City(
                code = DTO_BCN.code,
                name = DTO_BCN.name,
                country = COUNTRY_ES,
                workingArea = listOf(listOf(LatLng(3.0, -3.0)))
            )

    }

}
