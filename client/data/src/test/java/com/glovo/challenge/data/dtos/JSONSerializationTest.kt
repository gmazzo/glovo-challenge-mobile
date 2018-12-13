package com.glovo.challenge.data.dtos

import com.glovo.challenge.data.BaseTest
import com.glovo.challenge.data.cities.CityDTO
import com.glovo.challenge.data.countries.CountryDTO
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class JSONSerializationTest(
    private val jsonFile: String,
    private val targetClass: KClass<*>
) : BaseTest() {
    private val gson = Gson()

    @Test
    fun reserializationTest() {
        val json = javaClass.classLoader!!.getResource(jsonFile).readText()
        val expected = gson.fromJson(json, JsonElement::class.java)

        val parsed = gson.fromJson(json, targetClass.java)
        val reserializedJson = gson.toJsonTree(parsed)

        assertEquals(expected, reserializedJson)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(
            arrayOf("countries.json", Array<CountryDTO>::class),
            arrayOf("cities.json", Array<CityDTO>::class),
            arrayOf("cities-BUE.json", CityDTO::class),
            arrayOf("cities-BCN.json", CityDTO::class)
        )

    }

}
