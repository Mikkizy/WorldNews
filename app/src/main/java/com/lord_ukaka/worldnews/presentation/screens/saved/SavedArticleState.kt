package com.lord_ukaka.worldnews.presentation.screens.saved

import com.lord_ukaka.worldnews.domain.models.NewsArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SavedArticleState(
    val isLoading: Boolean = false,
    val articles: Flow<List<NewsArticle>> = flow { emptyList<NewsArticle>() }
)
