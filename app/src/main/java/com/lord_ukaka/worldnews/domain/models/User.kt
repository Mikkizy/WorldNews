package com.lord_ukaka.worldnews.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int?,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val gender: String,
    val country: String,
    val token: String? = null,
)