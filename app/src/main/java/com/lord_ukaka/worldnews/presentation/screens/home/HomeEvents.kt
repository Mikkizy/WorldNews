package com.lord_ukaka.worldnews.presentation.screens.home

import com.lord_ukaka.worldnews.domain.models.NewsCategory

sealed class HomeEvents {
    object OnDownloadIconClicked: HomeEvents()
    object OnPostIconClicked: HomeEvents()
    data class OnCategoryClicked(val category: NewsCategory): HomeEvents()
    object OnMoreInfoIconClicked: HomeEvents()
}
