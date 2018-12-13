package com.glovo.challenge.data.countries

import com.google.gson.annotations.SerializedName

internal data class CountryDTO(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String
)
