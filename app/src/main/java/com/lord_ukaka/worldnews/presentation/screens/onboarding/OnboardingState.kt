package com.lord_ukaka.worldnews.presentation.screens.onboarding

import androidx.annotation.DrawableRes
import com.lord_ukaka.worldnews.R

data class OnboardingState(
    val title: String = "",
    @DrawableRes val image: Int = R.drawable.ic_breaking_news_1,
    val description: String = ""
)
