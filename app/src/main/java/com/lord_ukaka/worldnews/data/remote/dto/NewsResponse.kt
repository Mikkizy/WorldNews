package com.lord_ukaka.worldnews.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)