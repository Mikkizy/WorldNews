package com.lord_ukaka.worldnews.presentation.screens.profile

import android.net.Uri

data class ProfileState(
    val username: String = "Miracle Ukaka",
    val email: String = "ukakamiracle@gmail.com",
    val phoneNumber: String = "+2348108392892",
    var profilePicture: Uri? = null
)
