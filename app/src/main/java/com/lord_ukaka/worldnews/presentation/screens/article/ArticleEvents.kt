package com.lord_ukaka.worldnews.presentation.screens.article

import com.lord_ukaka.worldnews.domain.models.NewsArticle

sealed class ArticleEvents {
    object OnMoreIconClicked: ArticleEvents()
    object OnFloatingActionButtonClicked: ArticleEvents()
    data class OnShareButtonClicked(val article: NewsArticle): ArticleEvents()
    object OnSaveButtonClicked: ArticleEvents()
}
