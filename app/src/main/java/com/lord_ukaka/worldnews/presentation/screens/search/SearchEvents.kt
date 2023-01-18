package com.lord_ukaka.worldnews.presentation.screens.search

sealed class SearchEvents {
    data class OnSearchQueryEntered(val query: String): SearchEvents()
    object OnSearchTextClicked: SearchEvents()
}
