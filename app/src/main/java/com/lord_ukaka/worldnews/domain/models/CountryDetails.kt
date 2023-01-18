package com.lord_ukaka.worldnews.domain.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lord_ukaka.worldnews.NewsApplication
import com.lord_ukaka.worldnews.core.utils.getJsonData
import java.lang.reflect.Type

data class CountryDetails(
    val name: String,
    val dial_code: String,
    val code: String
)