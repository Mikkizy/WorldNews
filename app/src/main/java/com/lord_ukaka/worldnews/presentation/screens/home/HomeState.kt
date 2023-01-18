package com.lord_ukaka.worldnews.presentation.screens.home

import androidx.paging.PagingData
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.NewsCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class HomeState(
    val isLoading: Boolean = false,
    val articles: Flow<PagingData<NewsArticle>> = flow { PagingData.empty<NewsArticle>() },
    val error: String = "",
    val category: NewsCategory = NewsCategory("General", "general")
)
