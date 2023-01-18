package com.lord_ukaka.worldnews.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String,
    val name: String
)