package com.glovo.challenge.data.cities

import com.google.gson.annotations.SerializedName

internal data class CityDTO(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("currency") val currency: String?,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("enabled") val enabled: Boolean?,
    @SerializedName("time_zone") val timeZone: String?,
    @SerializedName("working_area") val workingArea: List<String>,
    @SerializedName("busy") val busy: Boolean?,
    @SerializedName("language_code") val languageCode: String?
)
