package com.lord_ukaka.worldnews.presentation.screens.article

import androidx.compose.ui.graphics.ImageBitmap
import com.lord_ukaka.worldnews.domain.models.NewsArticle

data class ArticleState(
    val article: NewsArticle? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    var floatingActionButtonState: FloatingActionButtonState = FloatingActionButtonState.Collapsed
)

enum class FloatingActionButtonState {
    Collapsed,
    Expanded
}

class FabOptionItem(
    val icon: ImageBitmap,
    val label: String
)
