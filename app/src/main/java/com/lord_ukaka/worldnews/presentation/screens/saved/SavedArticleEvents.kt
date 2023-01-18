package com.lord_ukaka.worldnews.presentation.screens.saved

import com.lord_ukaka.worldnews.domain.models.NewsArticle

sealed class SavedArticleEvents {
    object OnDeleteIconClicked: SavedArticleEvents()
    object RestoreArticle: SavedArticleEvents()
    object RestoreArticles: SavedArticleEvents()
    data class OnArticleSwiped(val article: NewsArticle): SavedArticleEvents()
}
