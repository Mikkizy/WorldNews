package com.lord_ukaka.worldnews.presentation.screens.search

import androidx.paging.PagingData
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchState(
    val isLoading: Boolean = false,
    val error: String = "",
    val searchQuery: String = "",
    val searchResults: Flow<PagingData<NewsArticle>> = flow { PagingData.empty<NewsArticle>() }
)
