package com.glovo.challenge.data.models

data class City(
    val code: String,
    val name: String,
    val country: Country,
    val workingArea: List<String>
)
